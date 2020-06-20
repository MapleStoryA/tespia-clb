/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import net.tespia.data.FieldInfo;
import net.tespia.data.FieldInfoFactory;
import net.tespia.data.inventory.ItemSlotBase;
import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;
import net.tespia.packets.ReceiveOps;
import net.tespia.packets.SendOps;
import net.tespia.ui.FieldManager;

import java.util.Collection;

public class Field {

    private final int id;
    private final FieldManager manager;
    private final MobPool mobPool;
    private final UserPool userPool;
    private final ShopPool shopPool;
    private final FieldInfo info;
    private byte fieldKey;

    public Field(FieldManager manager, int id) {
        this.id = id;
        this.manager = manager;
        this.shopPool = new ShopPool(manager, this);
        this.mobPool = new MobPool(manager, this);
        this.userPool = new UserPool(manager, this);
        this.info = FieldInfoFactory.getInstance().getInfo(id);
    }

    public FieldInfo getInfo() {
        return info;
    }


    public Collection<Shop> getShops() {
        return shopPool.getValues();
    }

    public void onPacket(short opcode, User user, InPacket in) {
        switch (opcode) {
            case ReceiveOps.SHOW_HIRED_MERCHANT: {
                Shop shop = this.shopPool.OnEmployeeEnterField(user, in);
                this.manager.onEvent("employeeEnterField", shop);
                break;
            }
            case ReceiveOps.REMOVE_HIRED_MERCHANT: {
                this.shopPool.OnEmployeeLeaveField(user, in);
                break;
            }
            case ReceiveOps.SHOW_MONSTER:
                this.mobPool.OnMobEnterField(user, in);
                break;
            case ReceiveOps.REMOVE_MONSTER: {
                this.mobPool.OnMobLeaveField(user, in);
            }
        }
    }

    public void setCurrentFieldKey(byte key) {
        this.fieldKey = key;
    }

    public byte getFieldKey() {
        return this.fieldKey;
    }

    public void attackRandomMonster(User user, Mob mob) {
        OutPacket out = new OutPacket(SendOps.MELEE_ATTACK);
        out.encodeByte(0); // Portals
        out.encodeByte(17); // numAttackedDmg
        out.encodeInt(0); // skill
        //out.encodeInt(0); // charge
        out.encodeByte(0); // Display
        out.encodeByte(0); // Stance
        out.encodeByte(0); // Weapon class
        out.encodeByte(0); // Speed
        out.encodeInt((int) System.currentTimeMillis()); // tick
        for (int i = 0; i < 1; i++) {
            out.encodeInt(mob.getId());
            out.encodeInt(0);
            out.encodePosition(mob.getPos());
            out.encodePosition(mob.getPos());
            out.encodeShort(0);
            out.encodeInt(5); //  DMG
            out.encodeInt(0);
        }
        user.send(out);
    }


    public void joinShop(User user, InPacket in) {
        int type = in.decodeByte();
        if (!(type == 5)) return;
        int maxPlayers = in.decodeByte();
        int pos = in.decodeByte();
        boolean isOwner = in.decodeByte() == 1;
        if (isOwner) {
            return;
        }
        int style = in.decodeInt();
        String text = in.decodeString();
        while (in.decodeByte() > 0) {
            AvatarLook look = new AvatarLook();
            look.decode(in);
            String visitorName = in.decodeString();
            System.out.println(visitorName);
        }
        short numberOfChats = in.decodeShort();
        for (int i = 0; i < numberOfChats; i++) {
            String line = in.decodeString();
            in.decodeByte();
        }
        String owner = in.decodeString();
        String description = in.decodeString();
        in.decodeByte();
        in.decodeInt();
        int itemCount = in.decodeByte();
        for (int i = 0; i < itemCount; i++) {
            int shopPos = in.decodeShort();
            short qty = in.decodeShort();
            int price = in.decodeInt();
            ItemSlotBase slot = ItemSlotBase.decode(in);
            ShopItem item = new ShopItem(shopPos, qty, price);
            item.setSlot(slot);
            //System.out.println("Item: " + slot.getItemID() + " Price: " + price);
            Shop shop = shopPool.get(user.getCurrentShopId());
            if(shop != null){
                shop.addItem(item);
            }
        }
    }
}
