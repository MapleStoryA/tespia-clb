package net.tespia.data;

import net.tespia.common.Position;


public class LifeInfo {
  private int templateID;
  private int cy;
  private int f;
  private int foothold;
  private int rx0;
  private int rx1;
  private Position position;
  private boolean isHide;
  private int mTime;
  private String cType;
  private String type = "";
  private MobInfo mobInfo;

  public boolean isNPC() {
    return this.type.equals("n");
  }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getFoothold() {
        return foothold;
    }

    public void setFoothold(int foothold) {
        this.foothold = foothold;
    }

    public int getRx0() {
        return rx0;
    }

    public void setRx0(int rx0) {
        this.rx0 = rx0;
    }

    public int getRx1() {
        return rx1;
    }

    public void setRx1(int rx1) {
        this.rx1 = rx1;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isIsHide() {
        return isHide;
    }

    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MobInfo getMobInfo() {
        return mobInfo;
    }

    public void setMobInfo(MobInfo mobInfo) {
        this.mobInfo = mobInfo;
    }


}


