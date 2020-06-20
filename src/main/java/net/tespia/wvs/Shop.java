package net.tespia.wvs;

import net.tespia.common.Position;

import java.util.Collection;
import java.util.HashMap;

public class Shop {
    private final int templateID;
    private final int id;
    private Position pos;
    private int foothold;
    private String owner;
    private String text;
    private int shopPosition = 0;
    private HashMap<Integer, ShopItem> items = new HashMap<>();

    public Shop(int id, int templateID) {
        this.id = id;
        this.templateID = templateID;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setFoothold(int foothold) {
        this.foothold = foothold;
    }

    public void setOwner(String text) {
        this.owner = text;
    }

    public int getTemplateID() {
        return templateID;
    }

    public Position getPos() {
        return pos;
    }

    public int getFoothold() {
        return foothold;
    }

    public String getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public Collection<ShopItem> getItems() {
        return items.values();
    }

    public void addItem(ShopItem item) {
        this.items.put(shopPosition++, item);
    }
}
