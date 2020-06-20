package net.tespia.data.inventory;

import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;

public class ItemSlotEquip extends ItemSlotBase {

    private long sn; // ID
    private int ruc; // Slot
    private int cuc; // Scroll slot
    private short str;
    private short dex;
    private short int_;
    private short luk;
    private short maxHP;
    private short maxMP;
    private short pad;
    private short mad;
    private short pdd;
    private short mdd;
    private short acc;
    private short eva;
    private short craft;
    private short speed;
    private short jump;
    private String title;
    private short attribute;

    @Override
    int getType() {
        return 1;
    }

    @Override
    public void rawDecode(InPacket in) {
        super.rawDecode(in);
        this.ruc = in.decodeByte();
        this.cuc = in.decodeByte();
        this.str = in.decodeShort();
        this.dex = in.decodeShort();
        this.luk = in.decodeShort();
        this.int_ = in.decodeShort();
        this.maxHP = in.decodeShort();
        this.maxMP = in.decodeShort();
        this.pad = in.decodeShort();
        this.mad = in.decodeShort();
        this.pdd = in.decodeShort();
        this.mdd = in.decodeShort();
        this.acc = in.decodeShort();
        this.eva = in.decodeShort();
        this.craft = in.decodeShort();
        this.speed = in.decodeShort();
        this.jump = in.decodeShort();
        this.title = in.decodeString();
        this.attribute = in.decodeShort();
        if (!isCashItem()) {
            this.sn = in.decodeLong();
        }
    }

    @Override
    public void rawEncode(OutPacket out) {
        super.rawEncode(out);
        out.encodeByte(this.ruc);
        out.encodeByte(this.cuc);
        out.encodeShort(this.str);
        out.encodeShort(this.dex);
        out.encodeShort(this.luk);
        out.encodeShort(this.int_);
        out.encodeShort(this.maxHP);
        out.encodeShort(this.maxMP);
        out.encodeShort(this.pad);
        out.encodeShort(this.mad);
        out.encodeShort(this.pdd);
        out.encodeShort(this.mdd);
        out.encodeShort(this.acc);
        out.encodeShort(this.eva);
        out.encodeShort(this.craft);
        out.encodeShort(this.speed);
        out.encodeShort(this.jump);
        out.encodeString(this.title);
        out.encodeShort(this.attribute);
        if (!this.isCashItem()) {
            out.encodeLong(this.sn);
        }
    }

    void setProtected() {
        this.attribute |= 1;
    }

    void setPreventSlip() {
        this.attribute |= 2;
    }

    void setWarmSupport() {
        this.attribute |= 4;
    }

    void setBinded() {
        this.attribute |= 8;
    }

    void setPossibleTrading() {
        this.attribute |= 0x10;
    }

    public static ItemInventory<ItemSlotEquip> newOf() {
        return new ItemInventory<>(MapleInventoryType.EQUIP);
    }

}
