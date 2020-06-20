package net.tespia.wvs;

import net.tespia.net.InOutEntity;
import net.tespia.net.OutPacket;
import net.tespia.net.InPacket;

public class AvatarLook implements InOutEntity<AvatarLook> {

    private int gender;
    private int skin;
    private int face;
    private int[] anHairEquip = new int[30];
    private int[] anUnseenEquip = new int[30];
    private int weaponStickerID;
    private int petID[] = new int[3];

    @Override
    public AvatarLook decode(InPacket in) {
        this.gender = in.decodeByte();
        this.skin = in.decodeByte();
        this.face = in.decodeInt();
        in.decodeByte();
        anHairEquip[0] = in.decodeInt();

        for (int i = 1; i <= this.anHairEquip.length; i++) {
            byte pos = in.decodeByte();
            if (pos == -1) {
                break;
            }
            int itemID = in.decodeInt();
            if (pos <= 0x3B && is_correct_bodypart(itemID, pos, 2)) {
                this.anHairEquip[pos] = itemID;
            }
        }

        for (int i = 1; i <= this.anHairEquip.length; i++) {
            byte pos = in.decodeByte();
            if (pos == -1) {
                break;
            }
            int itemID = in.decodeInt();
            if (pos <= 0x3B && is_correct_bodypart(itemID, pos, 2)) {
                this.anUnseenEquip[pos] = itemID;
            }
        }
        this.weaponStickerID = in.decodeInt();
        this.petID[0] = in.decodeInt();
        this.petID[1] = in.decodeInt();
        this.petID[2] = in.decodeInt();

        return this;
    }

    @Override
    public void encode(OutPacket out) {
        out.encodeByte(this.gender);
        out.encodeByte(this.skin);
        out.encodeInt(this.face);
        out.encodeByte(0);
        out.encodeInt(this.anHairEquip[0]);
        for (int i = 1; i < this.anHairEquip.length; i++) {
            int var = this.anHairEquip[i];
            if (var != 0) {
                out.encodeByte(i);
                out.encodeInt(var);
            }
        }
        out.encodeByte(0xFF);

        for (int i = 1; i < this.anUnseenEquip.length; i++) {
            int var = this.anUnseenEquip[i];
            if (var != 0) {
                out.encodeByte(i);
                out.encodeInt(var);
            }

        }
        out.encodeByte(0xFF);
        out.encodeInt(this.weaponStickerID);
        out.encodeInt(this.petID[0]);
        out.encodeInt(this.petID[1]);
        out.encodeInt(this.petID[2]);

    }

    boolean is_correct_bodypart(int nItemID, int nBodyPart, int nGender) {
        int v3; // edx@2
        int v4; // eax@7
        int v7; // eax@34

        if (nItemID / 1000000 == 1) {
            v3 = nItemID / 1000 % 10;
        } else {
            v3 = 2;
        }
        if (nGender != 2 && v3 != 2 && nGender != v3) {
            return false;
        }
        v4 = nItemID / 10000;
        if (nItemID / 10000 > 119) {
            if (v4 == 180) {
                if (nItemID != 1802100) {
                    return nBodyPart == 14;
                }
            } else {
                if (v4 == 181) {
                    if (nItemID == 1812000) {
                        return nBodyPart == 23;
                    }
                    if (nItemID == 1812001) {
                        return nBodyPart == 22;
                    }
                    if (nItemID == 1812002) {
                        return nBodyPart == 24;
                    }
                    if (nItemID == 1812003) {
                        return nBodyPart == 25;
                    }
                    if (nItemID == 1812004) {
                        return nBodyPart == 26;
                    }
                    if (nItemID == 1812005) {
                        return nBodyPart == 27;
                    }
                    if (nItemID == 1812006) {
                        return nBodyPart == 28;
                    }
                    return false;
                }
                if (v4 != 182) {
                    if (v4 == 183) {
                        return nBodyPart == 29;
                    }
                    if (v4 == 190) {
                        return nBodyPart == 18;
                    }
                    if (v4 == 191) {
                        return nBodyPart == 19;
                    }
                    if (v4 == 192) {
                        return nBodyPart == 20;
                    }
                    v7 = v4 / 10;
                    if (v7 == 13 || v7 == 14 || v7 == 16 || v7 == 17) {
                        return nBodyPart == 11;
                    }
                    return false;
                }
            }
            return nBodyPart == 21;
        }
        if (nItemID / 10000 != 119) {
            switch (v4) {
                case 100:
                    return nBodyPart == 1;
                case 101:
                    return nBodyPart == 2;
                case 102:
                    return nBodyPart == 3;
                case 103:
                    return nBodyPart == 4;
                case 104:
                case 105:
                    return nBodyPart == 5;
                case 106:
                    return nBodyPart == 6;
                case 107:
                    return nBodyPart == 7;
                case 108:
                    return nBodyPart == 8;
                case 110:
                    return nBodyPart == 9;
                case 111:
                    if (nBodyPart != 12 && nBodyPart != 13 && nBodyPart != 15 && nBodyPart != 16) {
                        return false;
                    }
                    return true;
                case 112:
                    return nBodyPart == 17;
                case 109:
                    return nBodyPart == 10;
                default:
                    v7 = v4 / 10;
                    if (v7 == 13 || v7 == 14 || v7 == 16 || v7 == 17) {
                        return nBodyPart == 11;
                    }
                    return false;
            }
        }
        return nBodyPart == 10;
    }

    public void setHair(int hair) {
        this.anHairEquip[0] = hair;
    }

    public int getHair() {
        return this.anHairEquip[0];
    }

    public void setTop(int top) {
        this.anHairEquip[5] = top;
    }

    public int getTop() {
        return this.anHairEquip[5];
    }

    public void setBottom(int top) {
        this.anHairEquip[6] = top;
    }

    public int getBottom() {
        return this.anHairEquip[6];
    }

    public void setShoes(int shoes) {
        this.anHairEquip[7] = shoes;
    }

    public int getShoes() {
        return this.anHairEquip[7];
    }

    public void setWeapon(int top) {
        this.anHairEquip[11] = top;
    }

    public int getWeapon() {
        return this.anHairEquip[11];
    }

    public void setPosition(int pos, int itemID) {
        this.anHairEquip[pos] = itemID;
    }
}
