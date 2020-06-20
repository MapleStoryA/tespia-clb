package net.tespia.data;

import net.tespia.common.Position;


public class PortalInfo {
    private int id;
    public String name;
    public String target;
    public int targetMapID;
    public Position position;
    public PortalType type;
    public int x, y;
    public static final int MAP_PORTAL = 2;
    public static final int DOOR_PORTAL = 6;
    private int foothold;

    public int getFoothold() {
        return foothold;
    }

    public void setFoothold(int foothold) {
        this.foothold = foothold;
    }

    public enum PortalType {
        DOOR,
        PORTAL
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getTargetMapID() {
        return targetMapID;
    }

    public void setTargetMapID(int targetMapID) {
        this.targetMapID = targetMapID;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PortalType getType() {
        return type;
    }

    public void setType(PortalType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
