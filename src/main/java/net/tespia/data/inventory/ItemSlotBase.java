package net.tespia.data.inventory;

import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;

public abstract class ItemSlotBase {
    protected int POS;
    protected int itemID;
    protected long cashItemSN = -1;
    protected long dateExpire;

    public int getPOS() {
        return POS;
    }

    public void setPOS(int POS) {
        this.POS = POS;
    }

    public boolean isCashItem() {
        return this.cashItemSN != 0;
    }

    public boolean isTimeLimited() {
        return this.dateExpire != 0;
    }

    public void encode(OutPacket out) {
        out.encodeByte(this.getType());
        rawEncode(out);
    }

    public boolean isEquip() {
        return this.getClass().isAssignableFrom(ItemSlotEquip.class);
    }


    public static <T extends ItemSlotBase> T decode(InPacket in) {
        switch (in.decodeByte()) {
            case 1:
                ItemSlotEquip equip = new ItemSlotEquip();
                equip.rawDecode(in);
                return (T) equip;
            case 2: {
                ItemSlotBundle bundle = new ItemSlotBundle();
                bundle.rawDecode(in);
                return (T) bundle;
            }
        }
        throw new UnsupportedOperationException();
    }

    public void rawDecode(InPacket in) {
        this.itemID = in.decodeInt();
        if (in.decodeByte() > 0) {
            this.cashItemSN = in.decodeLong();
        } else {
            this.cashItemSN = 0;
        }
        long time = in.decodeLong();
        this.dateExpire = time;
    }

    public void rawEncode(OutPacket out) {
        out.encodeInt(itemID);
        out.encodeByte(isCashItem());
        if (isCashItem()) {
            out.encodeLong(cashItemSN);
        }
        out.encodeLong(dateExpire);
    }

    public int getItemID() {
        return itemID;
    }

    public long getCashItemSN() {
        return cashItemSN;
    }

    public long getDateExpire() {
        return dateExpire;
    }

    abstract int getType();
}
