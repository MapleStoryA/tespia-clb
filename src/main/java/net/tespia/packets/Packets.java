package net.tespia.packets;

import java.util.Random;
import net.tespia.common.HexTool;
import net.tespia.net.OutPacket;

public class Packets {

    /**
     * Sends a pong packet.
     */
    public static OutPacket getPong() {
        OutPacket mplew = new OutPacket(16);

        mplew.encodeShort(0x18);
        mplew.encodeInt(0x0);

        return mplew;
    }

   

    public static OutPacket getLoginPacket(String user, String password) {
        OutPacket out = new OutPacket(SendOps.LOGIN_PASSWORD);
        out.encodeString(user);
        out.encodeString(password);
        out.encodeArr(HexTool.getByteArrayFromHexString("00 00 00 00 00 00 51 F4 74 14 00 00 00 00 12 49"));
        out.encodeInt(0);
        out.encodeByte(2);
        out.encodeByte(0);
        out.encodeByte(0);
        out.encodeInt(0);

        return out;
    }

    public static OutPacket getCharListPacket(int server, int channel) {
        OutPacket mplew = new OutPacket(SendOps.CHARLIST_REQ);
        mplew.encodeByte(server);
        mplew.encodeByte(channel);

        return mplew;
    }

   
    public static String getRandomMAC() {
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);
        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {
            if (sb.length() > 0) {
                sb.append("-");
            } else { //first byte, we need to set some options
                b = (byte) (b | (byte) (0x01 << 6)); //locally adminstrated
                b = (byte) (b | (byte) (0x00 << 7)); //unicast
            }
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

}
