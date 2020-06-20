package net.tespia.wvs;

import net.tespia.data.inventory.ItemSlotBase;

public class ShopItem {
    private int pos;
    private short qty;
    private int price;
    private ItemSlotBase slot;

    public ShopItem(int pos, short qty, int price) {
        this.pos = pos;
        this.qty = qty;
        this.price = price;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public short getQty() {
        return qty;
    }

    public void setQty(short qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemSlotBase getSlot() {
        return slot;
    }

    public void setSlot(ItemSlotBase slot) {
        this.slot = slot;
    }
}
