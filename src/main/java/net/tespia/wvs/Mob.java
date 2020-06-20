/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tespia.wvs;

import net.tespia.common.Position;
import net.tespia.net.InPacket;

/**
 * @author sin
 */
public class Mob {
    private int id;
    private int templateID;
    private MobStat stat = new MobStat();
    private Position position;

    int getId() {
        return id;
    }

    static class MobStat {

        int BYTE1(int i) {
            return i & 0x000000FF;
        }

        int BYTE2(int i) {
            return i & 0x0000FF00;
        }

        int BYTE3(int i) {
            return i & 0x00FF0000;
        }


        void decodeTemporaryStat(int dwFlag, InPacket in) {
            if ((dwFlag & 1) > 0) {
                statDecode(in);
            }
            if ((dwFlag & 2) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 4) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 8) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 0x10) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 0x20) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 0x40) > 0) {
                statDecode(in);

            }
            if ((dwFlag & 0x80) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 1) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 2) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 4) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 8) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 0x10) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 0x20) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 0x40) > 0) {
                statDecode(in);

            }
            if ((BYTE1(dwFlag) & 0x80) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 4) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 8) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 1) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 2) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 0x20) > 0) {
                statDecode(in);

            }
            if ((BYTE2(dwFlag) & 0x40) > 0) {
                statDecode(in);

            }
            if ((BYTE3(dwFlag) & 1) > 0) {
                statDecode(in);

            }
            if ((BYTE3(dwFlag) & 2) > 0) {
                statDecode(in);

            }
            if ((BYTE3(dwFlag) & 4) > 0) {
                statDecode(in);

            }
        }

        private void statDecode(InPacket in) {
            in.decodeShort();
            in.decodeInt();
            in.decodeShort();
        }
    }


    public void decodeInitData(InPacket in) {
        id = in.decodeInt();
        in.decodeByte(); // calcDamageStatIndex
        templateID = in.decodeInt();
        int mask = in.decodeInt();
        stat = new MobStat();
        stat.decodeTemporaryStat(mask, in);
        position = in.decodePosition();
    }

    public Position getPos() {
        return this.position;
    }

    public int getTemplateID() {
        return this.templateID;
    }
}
