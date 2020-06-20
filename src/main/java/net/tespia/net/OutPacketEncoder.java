package net.tespia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OutPacketEncoder extends MessageToByteEncoder<OutPacket> {

  @Override
  protected void encode(ChannelHandlerContext ctx, OutPacket packet, ByteBuf buff) throws Exception {
    buff.writeInt(packet.getLength());
    buff.writeBytes(packet.getData());
  }
}
