/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import io.netty.channel.ChannelFuture;
import net.tespia.common.Position;
import net.tespia.data.FieldInfo;
import net.tespia.data.MapleFoothold;
import net.tespia.data.PortalInfo;
import net.tespia.data.inventory.ItemInventory;
import net.tespia.data.inventory.ItemSlotBundle;
import net.tespia.data.inventory.ItemSlotEquip;
import net.tespia.data.inventory.MapleInventoryType;
import net.tespia.net.ClientSocket;
import net.tespia.net.OutPacket;
import net.tespia.packets.SendOps;

import java.awt.*;

/**
 * @author sin
 */
public class User {

    private CharacterStat stat;
    private final ItemInventory<ItemSlotEquip> equips;
    private final ItemInventory<ItemSlotBundle> useInv;
    private final ItemInventory<ItemSlotBundle> setupInv;
    private final ItemInventory<ItemSlotBundle> etcInv;
    private final ItemInventory<ItemSlotBundle> cashInv;
    private Field field;
    private ClientSocket socket;
    private int mesos = 0;
    private Position position;
    private MapleFoothold foothold;
    private int currentShopID;

    public User() {
        equips = ItemSlotEquip.newOf();
        useInv = ItemSlotBundle.newOf(MapleInventoryType.USE);
        setupInv = ItemSlotBundle.newOf(MapleInventoryType.SETUP);
        etcInv = ItemSlotBundle.newOf(MapleInventoryType.ETC);
        cashInv = ItemSlotBundle.newOf(MapleInventoryType.CASH);
    }

    public ItemInventory<ItemSlotEquip> getEquips() {
        return equips;
    }

    public ItemInventory<ItemSlotBundle> getUseInv() {
        return useInv;
    }

    public ItemInventory<ItemSlotBundle> getSetupInv() {
        return setupInv;
    }

    public ItemInventory<ItemSlotBundle> getEtcInv() {
        return etcInv;
    }

    public ItemInventory<ItemSlotBundle> getCashInv() {
        return cashInv;
    }

    public void setStat(CharacterStat stat) {
        this.stat = stat;
    }

    public CharacterStat getStat() {
        return stat;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return this.field;
    }

    public ChannelFuture send(OutPacket out) {
        ChannelFuture send = this.socket.send(out);
        while (!send.isDone()) {
            System.out.println("Waiting for packet to be sent...");
            ;
        }
        return send;
    }

    public void setSocket(ClientSocket socket) {
        this.socket = socket;
    }

    public void setMesos(int mesos) {
        this.mesos = mesos;
    }

    public int getMesos() {
        return this.mesos;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public MapleFoothold getFoothold() {
        return foothold;
    }

    public void setFoothold(MapleFoothold foothold) {
        this.foothold = foothold;
    }

    public void enterShop(Shop shop) {
        this.currentShopID = shop.getId();
        OutPacket out = new OutPacket(SendOps.MINIROOM_ACT);
        out.encodeByte(0x4);
        out.encodeInt(shop.getId());
        out.encodeShort(0);
        send(out);
    }

    public void buyItem(Shop shop, int slot) {

    }

    public void leaveShop(Shop shop) {

        OutPacket out = new OutPacket(SendOps.MINIROOM_ACT);
        out.encodeByte(0xA);
        send(out).addListener(e -> {
            this.currentShopID = 0;
        });
    }

    public void registerTransferField(String portal) {
        FieldInfo portalInfo = field.getInfo();
        PortalInfo p = portalInfo.getPortals().get(portal);
        final MapleFoothold fh = field.getInfo()
                .getFootholds()
                .findBelow(new Point(p.getX(), p.getY()));

        if (p != null && fh.getId() == foothold.getId()) {
            OutPacket out = new OutPacket(SendOps.USE_INNER_PORTAL);
            out.encodeByte(this.field.getFieldKey());
            out.encodeInt(-1);
            out.encodeString(portal);
            out.encodeInt((int) System.currentTimeMillis());
        }

    }

    public Integer getCurrentShopId() {
        return currentShopID;
    }
}
