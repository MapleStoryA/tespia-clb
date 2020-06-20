package net.tespia.net;

public interface Decodeable<T> {
  T decode(InPacket in);
}
