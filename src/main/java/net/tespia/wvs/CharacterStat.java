package net.tespia.wvs;

import net.tespia.net.InOutEntity;
import net.tespia.net.InPacket;
import net.tespia.net.OutPacket;

public class CharacterStat implements InOutEntity<CharacterStat> {
  private int characterID;
  private String characterName;
  private int gender;
  private int skin;
  private int face;
  private int hair;
  private long aliPetLockerSN[] = new long[3];
  private int level;
  private int job;
  private int STR;
  private int DEX;
  private int INT;
  private int LUK;
  private int HP;
  private int MHP;
  private int MP;
  private int MMP;
  private int AP;
  private int SP;
  private int EXP;
  private int POP;
  private int money;
  private byte portal;
  private int fieldID;

  @Override
  public CharacterStat decode(InPacket in) {
    this.characterID = in.decodeInt();
    this.characterName = in.decodeString(13);
    this.gender = in.decodeByte();
    this.skin = in.decodeByte();
    this.face = in.decodeInt();
    this.hair = in.decodeInt();
    for (int i = 0; i < aliPetLockerSN.length; i++) {
      this.aliPetLockerSN[i] = in.decodeLong();
    }
    this.level = in.decodeByte() & 0xFF;
    this.job = in.decodeShort();
    this.STR = in.decodeShort();
    this.DEX = in.decodeShort();
    this.INT = in.decodeShort();
    this.LUK = in.decodeShort();
    this.HP = in.decodeShort();
    this.MHP = in.decodeShort();
    this.MP = in.decodeShort();
    this.MMP = in.decodeShort();
    this.AP = in.decodeShort();

    this.SP = in.decodeShort();

    this.EXP = in.decodeInt();
    this.POP = in.decodeShort();
    in.decodeInt();
    this.fieldID = in.decodeInt();
    this.portal = in.decodeByte();
    in.decodeInt();
    return this;
  }

  @Override
  public void encode(OutPacket out) {
    out.encodeInt(this.characterID);
    out.encodeString(this.characterName, 13);
    out.encodeByte(this.gender);
    out.encodeByte(this.skin);
    out.encodeInt(this.face);
    out.encodeInt(this.hair);
    for (int i = 0; i < aliPetLockerSN.length; i++) {
      out.encodeLong(this.aliPetLockerSN[i]);
    }
    out.encodeByte(this.level);
    out.encodeShort(this.job);
    out.encodeShort(this.STR);
    out.encodeShort(this.DEX);
    out.encodeShort(this.INT);
    out.encodeShort(this.LUK);
    out.encodeInt(this.HP);
    out.encodeInt(this.MHP);
    out.encodeInt(this.MP);
    out.encodeInt(this.MMP);
    out.encodeShort(this.AP);

    out.encodeShort(this.SP);

    out.encodeInt(this.EXP);
    out.encodeShort(this.POP);
    out.encodeInt(0);
    out.encodeInt(this.fieldID);
    out.encodeByte(this.portal);
  
  }

    public int getCharacterID() {
        return characterID;
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getGender() {
        return gender;
    }

    public int getSkin() {
        return skin;
    }

    public int getFace() {
        return face;
    }

    public int getHair() {
        return hair;
    }

    public long[] getAliPetLockerSN() {
        return aliPetLockerSN;
    }

    public int getLevel() {
        return level;
    }

    public int getJob() {
        return job;
    }

    public int getSTR() {
        return STR;
    }

    public int getDEX() {
        return DEX;
    }

    public int getINT() {
        return INT;
    }

    public int getLUK() {
        return LUK;
    }

    public int getHP() {
        return HP;
    }

    public int getMHP() {
        return MHP;
    }

    public int getMP() {
        return MP;
    }

    public int getMMP() {
        return MMP;
    }

    public int getAP() {
        return AP;
    }

    public int getSP() {
        return SP;
    }

    public int getEXP() {
        return EXP;
    }

    public int getPOP() {
        return POP;
    }

    public int getMoney() {
        return money;
    }

    

    public byte getPortal() {
        return portal;
    }

    
    public int getFieldID() {
        return fieldID;
    }


  
}
