/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.net;

import io.netty.channel.*;
import net.tespia.common.HexTool;
import net.tespia.common.Position;
import net.tespia.data.FieldInfo;
import net.tespia.data.MapleFoothold;
import net.tespia.data.PortalInfo;
import net.tespia.data.inventory.DBChar;
import net.tespia.data.inventory.ItemInventory;
import net.tespia.data.inventory.ItemSlotBundle;
import net.tespia.data.inventory.ItemSlotEquip;
import net.tespia.packets.Packets;
import net.tespia.packets.ReceiveOps;
import net.tespia.packets.SendOps;
import net.tespia.ui.LoginModel;
import net.tespia.wvs.*;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.UUID;


public class ClientSocket extends NetworkClient {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(NetworkClient.class);
    public static final int ACT_JOIN = 5;

    private final UIController controller;
    private MapleCipher sendCipher;
    private MapleCipher recvCipher;
    private Channel channel;
    private int packetLength = -1;
    private boolean isConnected = false;
    private Context context;
    private MigrationState state;

    public void setInGame() {
        state = MigrationState.IN_GAME;
    }

    public void changeChannel(short ch) {
        if (ch > context.getMaxChannel()) {
            this.controller.log("Could not change to channel %d", ch);
            return;
        }
        OutPacket out = new OutPacket(SendOps.CHANGE_CHANNEL);
        out.encodeByte(ch);
        out.encodeInt((int) System.currentTimeMillis());
        send(out);
    }

    public void sendPasswordPacket(LoginModel m) {
        send(Packets.getLoginPacket(m.getUserID(), m.getPassword()));
    }

    enum MigrationState {
        LOGIN,
        CONNECTED,
        IN_GAME
    }

    public ClientSocket(String host, int port, UIController listener) {
        super(host, port);
        this.controller = listener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MapleCipher getSendCipher() {
        return this.sendCipher;
    }

    public MapleCipher getReceiveCipher() {
        return this.recvCipher;
    }

    int getPacketLength() {
        return this.packetLength;
    }

    void setPacketLength(int len) {
        this.packetLength = len;
    }

    @Override
    protected void onConnectionClosed(ChannelHandlerContext ctx) {
        this.isConnected = false;
        this.context = null;
        controller.onConnectionClosed();
    }

    public void setConnected(boolean value) {
        this.isConnected = value;
    }

    @Override
    protected void onConnectionOpened(ChannelHandlerContext ctx) {
        setChannel(ctx.channel());
        controller.onConnectionOpened();
        LoginModel m = controller.getLoginModel();
        if (this.state == MigrationState.IN_GAME) {
            this.sendMigrateToChannel();
            this.controller.setInGame();
            this.context.updateSocket(this);
        } else {
            this.state = MigrationState.LOGIN;
            //send(Packets.getLoginPacket(m.getUserID(), m.getPassword()));
        }

    }

    @Override
    protected void onConnectFailure(Exception e) {
        this.isConnected = false;
        controller.onConnectFailure();
        this.context = null;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    void setChannel(Channel channel) {
        this.channel = channel;
    }

    void setReceiveIV(MapleCipher cipherRecv) {
        this.recvCipher = cipherRecv;
    }

    void setSendIV(MapleCipher cipherSend) {
        this.sendCipher = cipherSend;
    }

    public ChannelFuture send(OutPacket out) {
        return channel.writeAndFlush(out).addListener(e -> {
            if (e.isSuccess()) {
                // listener.log("Packet sent.");
            } else {
                controller.log("Could not write packet: " + e.cause());
            }
        });
    }

    private void onPacket(InPacket in) {
        LoginModel m = controller.getLoginModel();
        short opcode = in.decodeShort();
        System.out.println("Received opcode: " + HexTool.toString(opcode));
        switch (opcode) {
            case ReceiveOps.PING: {
                send(new OutPacket(SendOps.PONG));
                controller.log("Received PING");
                break;
            }
            case ReceiveOps.LOGIN_RESULT: {
                short status = in.decodeByte();
                in.decodeByte();
                in.decodeInt();
                if (status == 0) {
                    controller.log("ID Accepted: " + m.getUserID());
                    context = new Context();
                    context.setAccountID(in.decodeInt());
                    context.setGender(in.decodeByte());
                    context.setGradeCode(in.decodeByte());
                    context.setCountryID(in.decodeByte());
                    context.setUserID(in.decodeString());
                    context.setPurchaseExp(in.decodeByte());
                    context.setChatBlockReason(in.decodeByte());
                    in.decodeLong();
                    in.decodeLong();
                    OutPacket out = new OutPacket(SendOps.PIN_OPERATION);
                    out.encodeByte(1);
                    out.encodeByte(0);
                    out.encodeInt(context.getAccountID());
                    out.encodeString(m.getPin());
                    send(out);
                } else if (status == 2) {
                    controller.log("Account has been banned.");
                    in.decodeByte();
                    in.decodeLong();
                }

                break;
            }
            case ReceiveOps.PIN_RESPONSE: {
                int code = in.decodeByte();
                if (code == 0 || code == 2) {
                    controller.log("Authenticated succesfully with account: " + m.getUserID());
                    this.state = MigrationState.CONNECTED;
                    send(new OutPacket(SendOps.SERVERLIST_REQUEST));
                } else {
                    controller.log("Server rejected the pin code.");
                }
                break;
            }
            case ReceiveOps.WORLD_ENTRY: {
                int worldID = in.decodeByte();
                if (worldID == -1) {
                    OutPacket out = new OutPacket(SendOps.REQ_SERVERLOAD);
                    out.encodeShort(0);
                    send(out);
                    break;
                }
                String name = in.decodeString();
                int flag = in.decodeByte();
                String channelMsg = in.decodeString();
                in.decodeInt(); // Rates.
                in.decodeByte();
                byte maxChannel = in.decodeByte();
                context.setMaxChannel(maxChannel);
                controller.updateContext(context);
                controller.log("[%d] channel(s) available, [%s]-[%s]:  Flag: %d Event: %s", maxChannel, name, worldID, flag, channelMsg);
                break;
            }
            case ReceiveOps.SERVERLOAD_MSG: {
                short status = in.decodeShort();
                if (status == 0 || status == 256) {
                    controller.log("Requesting list of chars.");
                    send(Packets.getCharListPacket(m.getWorldIndex(), m.getChannel()));
                } else {
                    controller.log("Server didn't returned server load correctly: %d.", status);
                }
                break;
            }
            case ReceiveOps.CHARLIST: {
                in.decodeByte();
                byte numberOfChars = in.decodeByte();
                controller.log("Number of characters in this account: %d.", numberOfChars);
                for (int i = 0; i < numberOfChars; i++) {
                    User user = new User();
                    CharacterStat stat = new CharacterStat();
                    stat.decode(in);
                    AvatarLook look = new AvatarLook();
                    look.decode(in);
                    // ranking
                    if (in.decodeByte() == 1) {
                        in.decodeInt();
                        in.decodeInt();
                        in.decodeInt();
                        in.decodeInt();
                    }

                    user.setStat(stat);
                    context.addUser(user);
                }
                controller.updateContext(context);
                LoginModel model = controller.getLoginModel();
                int selectedCharIndex = model.getCharIndex();
                User user = context.getUser(selectedCharIndex);
                OutPacket out = new OutPacket(SendOps.CHAR_SELECT);
                out.encodeInt(user.getStat().getCharacterID());
                String mac = Packets.getRandomMAC();
                String name = user.getStat().getCharacterName();
                out.encodeString(mac);
                String hhid = UUID.randomUUID().toString().replace("-", "");
                hhid = "988389752D15_51F47414";
                out.encodeString(hhid);

                this.controller.log("Selecting user %s, MAC: %s Serial: %s Lenght: %d", name, mac, hhid, out.getLength());

                send(out);
                break;
            }
            case ReceiveOps.GAME_HOST_ADDRESS: {
                in.decodeByte();
                byte ip[] = in.decodeArr(4);
                short port = in.decodeShort();
                this.migrateTo(ip, port);
                break;
            }
            case ReceiveOps.CHANNEL_ADDRESS: {
                in.decodeShort();
                byte ip[] = in.decodeArr(4);
                short port = in.decodeShort();
                int charID = in.decodeInt();
                this.migrateTo(ip, port);
                break;
            }
            case ReceiveOps.CHANGE_MAP: {
                onSetField(in);
                break;
            }
            case ReceiveOps.MINIROOM_ACT: {
                int id = in.decodeByte();
                switch (id) {
                    case ACT_JOIN: {
                        User user = controller.getSelectedUser();
                        Field field = user.getField();
                        field.joinShop(user, in);
                        break;
                    }
                }
                break;
            }
            case ReceiveOps.PLAYER_STAT_UPDATE: {
                in.decodeByte();
                int mask = in.decodeInt();
                if ((mask & 0x10000) > 0) {
                    this.controller.log("You have gained %d exp.", in.decodeInt());
                }
            }
            case ReceiveOps.MAP_CHAT: {
                break;
            }
            default: {
                User user = controller.getSelectedUser();
                Field field = user.getField();
                field.onPacket(opcode, user, in);
            }
        }


    }

    private void onSetField(InPacket in) {
        int ch = in.decodeInt();
        context.setCurrentChannel((byte) ch);
        byte fieldKey = in.decodeByte();
        boolean isCharData = in.decodeByte() == 1;
        in.decodeShort();
        if (isCharData) {
            in.decodeInt();
            in.decodeInt();
            in.decodeInt();
        }
        long mask = in.decodeLong();
        User user = this.controller.getSelectedUser();
        if ((mask & DBChar.CHARACTER) > 0) {
            CharacterStat stat = new CharacterStat();
            stat.decode(in);
            in.decodeByte();
            user.setStat(stat);

        }
        if ((mask & DBChar.MONEY) > 0) {
            user.setMesos(in.decodeInt());
        }
        final ItemInventory equips = user.getEquips();
        final ItemInventory<ItemSlotBundle> use = user.getUseInv();
        final ItemInventory<ItemSlotBundle> setup = user.getSetupInv();
        final ItemInventory<ItemSlotBundle> etc = user.getEtcInv();
        final ItemInventory<ItemSlotBundle> cash = user.getCashInv();

        if ((mask & DBChar.INVENTORYSIZE) > 0) {
            equips.setSlots(in.decodeByte());
            use.setSlots(in.decodeByte());
            setup.setSlots(in.decodeByte());
            etc.setSlots(in.decodeByte());
            cash.setSlots(in.decodeByte());
        }

        if ((mask & DBChar.ITEMSLOTEQUIP) > 0) {
            final ItemInventory<ItemSlotEquip> visibleEquips = ItemSlotEquip.newOf();
            visibleEquips.decode(in);

            //TODO: Fix pet equip.
            final ItemInventory<ItemSlotEquip> invisibleEquips = ItemSlotEquip.newOf();
            invisibleEquips.decode(in);

            equips.decode(in);
        }
        if ((mask & DBChar.ITEMSLOTCONSUME) > 0) {
            use.decode(in);
        }
        if ((mask & DBChar.ITEMSLOTINSTALL) > 0) {
            setup.decode(in);
        }
        if ((mask & DBChar.ITEMSLOTETC) > 0) {
            etc.decode(in);
        }
        if ((mask & DBChar.ITEMSLOTCASH) > 0) {
            cash.decode(in);
        }

        if ((mask & DBChar.SKILLRECORD) > 0) {
            for (short i = in.decodeShort(); i > 0; i--) {
                int skillID = in.decodeInt(); // id
                int level = in.decodeInt(); // level
                if (((skillID / 10000) % 10) == 2) {
                    in.decodeInt();
                }
                this.controller.log("Loaded skill: %d %d", skillID, level);
            }
        }

        if ((mask & DBChar.SKILLCOOLTIME) > 0) {
            // Cooldown
            for (short i = in.decodeShort(); i > 0; i--) {
                in.decodeInt(); // id
                in.decodeShort(); // level
            }
        }
        if ((mask & DBChar.QUESTRECORD) > 0) {
            // Started quests
            for (int i = in.decodeShort(); i > 0; i--) {
                short key = in.decodeShort();
                String info = in.decodeString();
                this.controller.log("Started quest: %d %s", key, info);
            }
        }
        if ((mask & DBChar.QUESTCOMPLETE) > 0) {
            for (short i = in.decodeShort(); i > 0; i--) {
                short key = in.decodeShort();
                long time = in.decodeLong();
                this.controller.log("Completed quest: %d %s", key, time);
            }
        }
        if ((mask & DBChar.MINIGAMERECORD) > 0) {
            for (short i = in.decodeShort(); i > 0; i--) {
                in.decodeInt();
                in.decodeInt();
                in.decodeInt();
                in.decodeInt();
                in.decodeInt();
            }
        }
        if ((mask & DBChar.COUPLERECORD) > 0) {
            // Couple rings
            for (int i = in.decodeShort(); i > 0; i--) {
                in.decodeInt(); // key
                in.decodeString();
                in.decodeLong();
                in.decodeLong();
            }
            // Friend rings
            for (short i = in.decodeShort(); i > 0; i--) {
                in.decodeInt(); // key
                in.decodeString();
                in.decodeLong();
                in.decodeLong();
                in.decodeInt();
            }
            // Wedding rings
            for (short i = in.decodeShort(); i > 0; i--) {
                in.decodeInt(); // key
                in.decodeString();
                in.decodeLong();
                in.decodeLong();
                in.decodeInt();
            }
        }
        if ((mask & DBChar.MAPTRANSFER) > 0) {
            // Rock maps
            for (short i = 0; i < 5; i++) {
                in.decodeInt(); // map id
            }
            // VIP maps
            for (short i = 0; i < 5; i++) {
                in.decodeInt(); // map id
            }

        }

        in.decodeInt();
        in.decodeLong();

        this.controller.updateContext(this.context);
        this.controller.log("Connected in Game in Channel %s", ch);


        Field field = user.getField();
        FieldInfo info = field.getInfo();
        field.setCurrentFieldKey(fieldKey);
        PortalInfo portal = null;

        portal = info.getPortalById(user.getStat().getPortal());

        this.controller.log("User will appear in portal %s.", user.getStat().getPortal());

        OutPacket out = new OutPacket(SendOps.MOVE_PLAYER);
        out.encodeByte(fieldKey);
        Position startPos = portal.getPosition();
        final MapleFoothold fh = field.getInfo()
                .getFootholds()
                .findBelow(new Point(startPos.getX(), startPos.getY()));
        user.setPosition(startPos);
        startPos = new Position(startPos.getX(), startPos.getY() - (startPos.getY() - fh.getY1()));
        user.setFoothold(fh);
        out.encodePosition(startPos);
        out.encodeByte(3); // Number or movements
        for (int i = 0; i < 3; i++) {
            int start_x = startPos.getX();
            int stance = 6;
            int wobble_y = 0;
            int duration = 400;
            int fh_id = 0;
            if (i > 1) {
                stance = 4;
            }
            if (i == 0) {
                duration = 90;
                wobble_y = 0;
            }
            if (i == 1) {
                duration = 20;
                wobble_y = 0;
            }
            if (i == 2) {
                wobble_y = 0;
                fh_id = fh.getId();
            }

            out.encodeByte(0); // Fall action
            out.encodePosition(new Position(start_x, startPos.getY()));
            out.encodePosition(new Position(0, wobble_y));
            out.encodeShort(fh_id);  // FH

            out.encodeByte(stance); // Stance
            out.encodeShort(duration);
        }
        int keys = 9;
        out.encodeByte(keys);
        for (int i = 0; i < keys; i++) {
            out.encodeByte(0);
        }

        out.encodePosition(startPos);
        out.encodePosition(startPos);
        send(out);
    }

    private void migrateTo(byte[] ip, short port) {
        controller.migrateTo(ip, port, this.context);
    }

    public void sendMigrateToChannel() {
        OutPacket out = new OutPacket(SendOps.PLAYER_CONNECTED);
        LoginModel model = this.controller.getLoginModel();
        User user = this.context.getUser(model.getCharIndex());
        out.encodeInt(user.getStat().getCharacterID());
        out.encodeShort(0);
        send(out);
    }

    static class ReadSocketHandler extends SimpleChannelInboundHandler<InPacket> {

        private ClientSocket client;

        public ReadSocketHandler(ClientSocket client) {
            this.client = client;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            client.controller.log("Exception happened.");
            //cause.printStackTrace();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, InPacket in) {
            if (in.getLength() < 2) {
                ctx.flush();
                return;
            }
            client.onPacket(in);
        }
    }

    @Override
    protected ChannelHandler[] getPipeline() {
        return new ChannelHandler[]{
                new CryptoInPacketDecoder(this),
                getInBoundHandler(),
                new CryptoOutPacketEncoder(this),
        };
    }

    @Override
    protected ChannelInboundHandler getInBoundHandler() {
        return new ReadSocketHandler(this);
    }

}
