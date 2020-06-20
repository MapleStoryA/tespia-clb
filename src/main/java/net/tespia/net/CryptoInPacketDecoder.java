package net.tespia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.tespia.common.HexTool;

import java.util.List;

public class CryptoInPacketDecoder extends ByteToMessageDecoder {

    private final ClientSocket socket;

    private boolean isInitialized = false;

    public CryptoInPacketDecoder(ClientSocket client) {
        this.socket = client;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (!socket.isConnected()) {
            in.readShortLE();
            in.readShortLE();
            int size = in.readShortLE();
            in.skipBytes(size);

            byte[] receiveIV = new byte[4];
            in.readBytes(receiveIV);

            byte[] sendIV = new byte[4];
            in.readBytes(sendIV);

            System.out.println("Recv: " + HexTool.toString(receiveIV));
            System.out.println("Send: " + HexTool.toString(sendIV));

            MapleCipher cipherSend = new MapleCipher(receiveIV, (short) 62);
            MapleCipher cipherRecv = new MapleCipher(sendIV, (short) 62);

            socket.setReceiveIV(cipherRecv);
            socket.setSendIV(cipherSend);
            socket.setConnected(true);
            socket.onConnectionOpened(ctx);

            return;
        }
        int packetLen = socket.getPacketLength();
        if (packetLen == -1) {
            if (in.readableBytes() >= 4) {
                if (!isInitialized) {
                    in.readByte();
                    isInitialized = true;
                }
                // System.out.println(HexTool.toString(in.array()));
                final int header = in.readInt();
                int len = MapleCipher.getPacketLength(header);
                socket.setPacketLength(len);
                //      return;
            } else {
                return;
            }
        }

        if (in.readableBytes() >= packetLen) {
            byte[] data = new byte[socket.getPacketLength()];
            in.readBytes(data);
            socket.setPacketLength(-1);
            socket.getReceiveCipher().crypt(data);
            MapleCustomEncryption.decryptData(data);
            InPacket packet = new InPacket(data);
            out.add(packet);
        }

    }
}
