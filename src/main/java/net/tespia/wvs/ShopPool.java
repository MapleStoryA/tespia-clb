package net.tespia.wvs;

import net.tespia.common.Position;
import net.tespia.net.InPacket;
import net.tespia.ui.FieldManager;

public class ShopPool extends EntityPool<Shop> {

    private final Field field;
    private final FieldManager manager;

    public ShopPool(FieldManager manager, Field field) {
        this.field = field;
        this.manager = manager;
    }

    public Shop OnEmployeeEnterField(User user, InPacket in) {
        int ownerID = in.decodeInt();
        int templateID = in.decodeInt();
        int x = in.decodeShort();
        int y = in.decodeShort();
        int foothold = in.decodeShort();
        String owner = in.decodeString();
        in.decodeByte();
        int id = in.decodeInt();
        String text = in.decodeString();
        Shop shop = new Shop(id, templateID);
        shop.setPos(new Position(x, y));
        shop.setFoothold(foothold);
        shop.setOwner(owner);
        shop.setText(text);
        this.insert(id, shop);
        return shop;

    }

    public void OnEmployeeLeaveField(User user, InPacket in) {
        int id = in.decodeInt();
        this.remove(id);
    }
}
