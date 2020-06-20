package net.tespia.net;

import net.tespia.common.FileTime;
import net.tespia.common.Position;
import net.tespia.common.Util;
import net.tespia.common.Rect;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OutPacket extends Packet implements Encodeable {
    
  private static final Logger log = LoggerFactory.getLogger(OutPacket.class);
  
  private ByteBuf baos;
  private boolean loopback = false;
  private boolean encryptedByShanda = false;
  private short op;

  /**
   * Creates a new OutPacket with a given op. Immediately encodes the op.
   *
   * @param op The opcode of this OutPacket.
   */
  public OutPacket(short op) {
    super(new byte[] {});
    baos = ByteBufAllocator.DEFAULT.buffer();
    encodeShort(op);
    this.op = op;
  }

  /**
   * Creates a new OutPacket with a given op. Immediately encodes the op.
   *
   * @param op The opcode of this OutPacket.
   */
  public OutPacket(int op) {
    this((short) op);
  }

  /**
   * Creates a new OutPacket, and initializes the data as empty.
   */
  public OutPacket() {
    super(new byte[] {});
    baos = ByteBufAllocator.DEFAULT.buffer();
  }

  /**
   * Creates a new OutPacket with given data.
   *
   * @param data The data this net.swordie.ms.connection.packet has to be initialized with.
   */
  public OutPacket(byte[] data) {
    super(data);
    baos = ByteBufAllocator.DEFAULT.buffer();
    encodeArr(data);
  }

  /**
   * Returns the header of this OutPacket.
   *
   * @return the header of this OutPacket.
   */
  @Override
  public int getHeader() {
    return op;
  }

  /**
   * Encodes a single byte to this OutPacket.
   *
   * @param b The int to rawEncode as a byte. Will be downcast, so be careful.
   */
  public void encodeByte(int b) {
    encodeByte((byte) b);
  }

  public void encodeByte(Callable<Integer> b) {
    try {
      encodeByte(b.call());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Encodes a byte to this OutPacket.
   *
   * @param b The byte to rawEncode.
   */
  public void encodeByte(byte b) {
    baos.writeByte(b);
  }

  /**
   * Encodes a byte array to this OutPacket.
   * Named like this to prevent autocompletion of "by" to "byteArray" or similar names.
   *
   * @param bArr The byte array to rawEncode.
   */
  public void encodeArr(byte[] bArr) {
    baos.writeBytes(bArr);
  }

  /**
   * Encodes a character to this OutPacket, UTF-8.
   *
   * @param c The character to rawEncode
   */
  public void encodeChar(char c) {
    baos.writeByte(c);
  }

  /**
   * Encodes a boolean to this OutPacket.
   *
   * @param b The boolean to rawEncode (0/1)
   */
  public void encodeByte(boolean b) {
    baos.writeBoolean(b);
  }

  /**
   * Encodes a short to this OutPacket, in little endian.
   *
   * @param s The short to rawEncode.
   */
  public void encodeShort(short s) {
    baos.writeShortLE(s);
  }

  public void encodeShort(Callable<Integer> s) {
    try {
      encodeShort(s.call());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void encodeShortBE(short s) {
    baos.writeShort(s);
  }

  public void encodeIntBE(int i) {
    baos.writeInt(i);
  }

  /**
   * Encodes an integer to this OutPacket, in little endian.
   *
   * @param i The integer to rawEncode.
   */
  public void encodeInt(int i) {
    baos.writeIntLE(i);
  }

  public void encodeInt(Callable<Integer> i) {
    try {
      encodeInt(i.call());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Encodes a long to this OutPacket, in little endian.
   *
   * @param l The long to rawEncode.
   */
  public void encodeLong(long l) {
    baos.writeLongLE(l);
  }

  /**
   * Encodes a String to this OutPacket.
   * Structure: short(size) + char array of <code>s</code>.
   *
   * @param s The String to rawEncode.
   */
  public void encodeString(String s) {
    if (s == null) {
      s = "";
    }
    if (s.length() > Short.MAX_VALUE) {
      log.error("Tried to rawEncode a string that is too big.");
      return;
    }
    encodeShort((short) s.length());
    encodeString(s, (short) s.length());
  }

  public void encodeString(Callable<String> s) {
    try {
      this.encodeString(s.call());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Writes a String as a character array to this OutPacket.
   * If <code>s.length()</code> is smaller than length, the open spots are filled in with zeros.
   *
   * @param s      The String to rawEncode.
   * @param length The maximum length of the buffer.
   */
  public void encodeString(String s, short length) {
    if (s == null) {
      s = "";
    }
    if (s.length() > 0) {
      for (char c : s.toCharArray()) {
        encodeChar(c);
      }
    }
    for (int i = s.length(); i < length; i++) {
      encodeByte((byte) 0);
    }
  }

  @Override
  public void setData(byte[] nD) {
    super.setData(nD);
    baos.clear();
    encodeArr(nD);
  }

  @Override
  public byte[] getData() {
    if (baos.hasArray()) {
      return baos.array();
    } else {
      byte[] arr = new byte[baos.writerIndex()];
      baos.nioBuffer().get(arr, 0, baos.writerIndex());
      return arr;
    }
  }

  @Override
  public Packet clone() {
    return new OutPacket(getData());
  }

  /**
   * Returns the length of the ByteArrayOutputStream.
   *
   * @return The length of baos.
   */
  @Override
  public int getLength() {
    return getData().length;
  }

  public boolean isLoopback() {
    return loopback;
  }

  public boolean isEncryptedByShanda() {
    return encryptedByShanda;
  }

  @Override
  public String toString() {
    return String.format("%s, %s/0x%s\t| %s", "", op, Integer.toHexString(op).toUpperCase()
        , Util.readableByteArray(Arrays.copyOfRange(getData(), 2, getData().length)));
  }

  public void encodeShort(int value) {
    encodeShort((short) value);
  }

  public void encodeString(String name, int length) {
    encodeString(name, (short) length);
  }

  public void encodeFT(FileTime fileTime) {
    if (fileTime == null) {
      encodeLong(0);
    } else {
      fileTime.encode(this);
    }
  }

  public void encodePosition(Position position) {
    if (position != null) {
      encodeShort(position.getX());
      encodeShort(position.getY());
    } else {
      encodeShort(0);
      encodeShort(0);
    }
  }

  public void encodeRectInt(Rect rect) {
    encodeInt(rect.getLeft());
    encodeInt(rect.getTop());
    encodeInt(rect.getRight());
    encodeInt(rect.getBottom());
  }

  public void encodePositionInt(Position position) {
    encodeInt(position.getX());
    encodeInt(position.getY());
  }

  public void encodeFT(long currentTime) {
    encodeFT(new FileTime(currentTime));
  }

  public void encodeTime(boolean dynamicTerm, int time) {
    encodeByte(dynamicTerm);
    encodeInt(time);
  }

  public void encodeTime(int time) {
    encodeByte(false);
    encodeInt(time);
  }

  public void release() {

  }

  @Override
  public void finalize() {
    this.baos.release();
  }

  public void encodeFT(LocalDateTime localDateTime) {
    encodeFT(FileTime.fromDate(localDateTime));
  }

  public void encode(Encodeable encodeable) {
    encodeable.encode(this);
  }


  public void encodeTime_Buff(Long tTime) {
    long tCur = System.currentTimeMillis();

    boolean bInterval = false;
    if (tTime >= tCur) {
      tTime -= tCur;
    } else {
      bInterval = true;
      tTime = tCur - tTime;
    }
    tTime /= 1000;//I believe Nexon uses seconds here.

    this.encodeByte(bInterval);
    this.encodeInt(tTime.intValue());
  }

  @Override
  public void encode(OutPacket out) {
    this.encodeArr(out.getData());
  }
}