package net.tespia.data.inventory;

import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;

public class ItemSlotBundle extends ItemSlotBase {

    private int number;
    private short attribute;
    private long SN;
    private String title;

    @Override
    int getType() {
        return 2;
    }

    @Override
    public void rawDecode(InPacket in) {
        super.rawDecode(in);
        this.number = in.decodeShort();
        this.title = in.decodeString();
        this.attribute = in.decodeShort();
        int type = this.itemID / 10000;
        if (type != 207 && type != 233) {
            this.SN = 0;
        } else {
            this.SN = in.decodeLong();
        }
    }

    @Override
    public void rawEncode(OutPacket out) {
        super.encode(out);
        out.encodeShort(this.number);
        out.encodeString(this.title);
        out.encodeShort(this.attribute);
        int type = this.itemID / 10000;
        if (type == 207 && type == 233) {
            out.encodeLong(this.SN);
        }
    }

    public static ItemInventory<ItemSlotBundle> newOf(MapleInventoryType type) {
        return new ItemInventory<>(type);
    }
}
