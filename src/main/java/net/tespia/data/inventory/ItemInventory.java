package net.tespia.data.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import net.tespia.net.InPacket;

public class ItemInventory<T extends ItemSlotBase> implements Iterable<T> {

    private final HashMap<Byte, T> inventory = new HashMap<>();
    private int slots = 60;
    private final MapleInventoryType type;

    public ItemInventory(MapleInventoryType type){
      this.type = type;
    }
    
    public void addItem(Byte pos, T item) {
        item.setPOS(pos);
        inventory.put(pos, item);
    }

    public T getItem(Byte pos) {
        return inventory.get(pos);
    }

    public T remove(Byte pos) {
        T remove = inventory.remove(pos);
        remove.setPOS(-1);
        return remove;
    }

    public void sort() {

    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public Iterator<T> iterator() {
        return inventory.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        inventory.values().forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return inventory.values().spliterator();
    }

    public void decode(InPacket in) {
        while (true) {
            byte pos = in.decodeByte();
            if (pos == 0) {
                break;
            }
            ItemSlotBase equip = ItemSlotBase.decode(in);
            equip.setPOS(pos);
            addItem(pos, (T) equip);
        }
    }

    public MapleInventoryType getType(){
       return this.type;
    }

    public int size() {
        return inventory.size();
    }
    
    
}
