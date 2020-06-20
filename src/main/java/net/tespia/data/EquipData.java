package net.tespia.data;


import java.io.Serializable;

public class EquipData implements Serializable {
  public int itemID;
  public int reqJob;
  public int reqLevel;
  public int reqSTR;
  public int reqDEX;
  public int reqINT;
  public int reqLUK;
  public int reqPOP;
  public int incSTR;
  public int incDEX;
  public int incINT;
  public int incLUK;
  public int incPDD;
  public int incMDD;
  public int incPAD;
  public int incMAD;
  public int tuc;
  public int price;
  public int cash;
  public int cursed;
  public int success;
  public int equipTradeBlock;
  public int durability;
  public int elemDefault;
  public int incRMAS;
  public int incRMAF;
  public int incRMAL;
  public int incRMAI;

  public EquipData() {
    if (isMagicWeapon(this.itemID)) {
      incRMAS = 100;
      incRMAF = 100;
      incRMAL = 100;
      incRMAI = 100;
    }
  }

  public static boolean isMagicWeapon(final int itemId) {
    final int s = itemId / 10000;
    return s == 137 || s == 138;
  }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getReqJob() {
        return reqJob;
    }

    public void setReqJob(int reqJob) {
        this.reqJob = reqJob;
    }

    public int getReqLevel() {
        return reqLevel;
    }

    public void setReqLevel(int reqLevel) {
        this.reqLevel = reqLevel;
    }

    public int getReqSTR() {
        return reqSTR;
    }

    public void setReqSTR(int reqSTR) {
        this.reqSTR = reqSTR;
    }

    public int getReqDEX() {
        return reqDEX;
    }

    public void setReqDEX(int reqDEX) {
        this.reqDEX = reqDEX;
    }

    public int getReqINT() {
        return reqINT;
    }

    public void setReqINT(int reqINT) {
        this.reqINT = reqINT;
    }

    public int getReqLUK() {
        return reqLUK;
    }

    public void setReqLUK(int reqLUK) {
        this.reqLUK = reqLUK;
    }

    public int getReqPOP() {
        return reqPOP;
    }

    public void setReqPOP(int reqPOP) {
        this.reqPOP = reqPOP;
    }

    public int getIncSTR() {
        return incSTR;
    }

    public void setIncSTR(int incSTR) {
        this.incSTR = incSTR;
    }

    public int getIncDEX() {
        return incDEX;
    }

    public void setIncDEX(int incDEX) {
        this.incDEX = incDEX;
    }

    public int getIncINT() {
        return incINT;
    }

    public void setIncINT(int incINT) {
        this.incINT = incINT;
    }

    public int getIncLUK() {
        return incLUK;
    }

    public void setIncLUK(int incLUK) {
        this.incLUK = incLUK;
    }

    public int getIncPDD() {
        return incPDD;
    }

    public void setIncPDD(int incPDD) {
        this.incPDD = incPDD;
    }

    public int getIncMDD() {
        return incMDD;
    }

    public void setIncMDD(int incMDD) {
        this.incMDD = incMDD;
    }

    public int getIncPAD() {
        return incPAD;
    }

    public void setIncPAD(int incPAD) {
        this.incPAD = incPAD;
    }

    public int getIncMAD() {
        return incMAD;
    }

    public void setIncMAD(int incMAD) {
        this.incMAD = incMAD;
    }

    public int getTuc() {
        return tuc;
    }

    public void setTuc(int tuc) {
        this.tuc = tuc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCursed() {
        return cursed;
    }

    public void setCursed(int cursed) {
        this.cursed = cursed;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getEquipTradeBlock() {
        return equipTradeBlock;
    }

    public void setEquipTradeBlock(int equipTradeBlock) {
        this.equipTradeBlock = equipTradeBlock;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getElemDefault() {
        return elemDefault;
    }

    public void setElemDefault(int elemDefault) {
        this.elemDefault = elemDefault;
    }

    public int getIncRMAS() {
        return incRMAS;
    }

    public void setIncRMAS(int incRMAS) {
        this.incRMAS = incRMAS;
    }

    public int getIncRMAF() {
        return incRMAF;
    }

    public void setIncRMAF(int incRMAF) {
        this.incRMAF = incRMAF;
    }

    public int getIncRMAL() {
        return incRMAL;
    }

    public void setIncRMAL(int incRMAL) {
        this.incRMAL = incRMAL;
    }

    public int getIncRMAI() {
        return incRMAI;
    }

    public void setIncRMAI(int incRMAI) {
        this.incRMAI = incRMAI;
    }

}
