package net.tespia.common;

import io.netty.buffer.ByteBuf;

import java.util.Random;

public class Util {
  /**
   * Returns a random number from <code>start</code> up to <code>end</code>. Creates a new Random class upon call.
   * If <code>start</code> is greater than <code>end</code>, <code>start</code> will be swapped with <code>end</code>.
   *
   * @param start the lower bound of the random number
   * @param end   the upper bound of the random number
   * @return A random number from <code>start</code> up to <code>end</code>
   */
  public static int getRandom(int start, int end) {
    if (end - start == 0) {
      return start;
    }
    if (start > end) {
      int temp = end;
      end = start;
      start = temp;
    }
    return start + new Random().nextInt(end - start);
  }

  /**
   * Turns a byte array into a readable String (e.g., 3A 00 89 BF).
   *
   * @param arr The array to transform
   * @return The readable byte array
   */
  public static String readableByteArray(byte[] arr) {
    StringBuilder res = new StringBuilder();
    for (byte b : arr) {
      res.append(String.format("%02X ", b));
    }
    return res.toString();
  }

  /**
   * Turns a ByteBuf into a readable String (e.g., 3A 00 89 BF).
   *
   * @param buf The ByteBuf to transform
   * @return The readable byte array
   */
  public static String readableByteArrayFromByteBuf(ByteBuf buf) {
    byte[] bytes = new byte[buf.capacity()];
    for (int i = buf.readableBytes(); i < buf.capacity(); i++) {
      bytes[i] = buf.getByte(i);
    }
    return readableByteArray(bytes);
  }

}

