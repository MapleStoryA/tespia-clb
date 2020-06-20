package org.tespia;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import net.tespia.net.MapleCipher;

public class IVHeaderCheckTest {

	
	public byte iv [] = { 70, 114, 122, 82 };
	
	public MapleCipher aes = new MapleCipher(iv, (short) 62);
	
	
	@Before
	public void init(){
		iv[3] = (byte) (Math.random() * 255);
	}
	
	@Test
	public void testHeader() {
		byte []  header = aes.getPacketHeader(16);
		int value = new BigInteger(header).intValue();
		boolean result = aes.checkPacket(value);
		Assert.assertTrue("Could not validate the header", result);
	}
}
