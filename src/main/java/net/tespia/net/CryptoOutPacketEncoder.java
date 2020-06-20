package net.tespia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CryptoOutPacketEncoder extends MessageToByteEncoder<OutPacket> {

   private final ClientSocket socket; 
    
   public CryptoOutPacketEncoder(ClientSocket socket){
     this.socket = socket;
   }
   
  @Override
  protected void encode(ChannelHandlerContext ctx, OutPacket packet, ByteBuf buff) throws Exception {
    if (socket != null && socket.isConnected()) {
      MapleCipher send_crypto = socket.getSendCipher();
      final byte[] inputInitialPacket = packet.getData();
      final byte[] unencrypted = new byte[inputInitialPacket.length];
      System.arraycopy(inputInitialPacket, 0, unencrypted, 0, inputInitialPacket.length);
      final byte[] ret = new byte[unencrypted.length + 4];
      //final Lock mutex = client.getLock();
      //mutex.lock();
      try {
        final byte[] header = send_crypto.getPacketHeader(unencrypted.length);
        MapleCustomEncryption.encryptData(unencrypted);
        send_crypto.crypt(unencrypted);
        System.arraycopy(header, 0, ret, 0, 4);
        System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length);
        buff.writeBytes(ret);
      } finally {
        //   mutex.unlock();
      }
    } else {
      buff.writeBytes(packet.getData());
    }

  }
}
