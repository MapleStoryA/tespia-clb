package net.tespia.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class InPacketDecoder extends ByteToMessageDecoder {


  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() > 1) {
      byte[] data = new byte[in.readableBytes()];
      in.readBytes(data);
      InPacket pkt = new InPacket(data);
      out.add(pkt);
    }

  }
}
