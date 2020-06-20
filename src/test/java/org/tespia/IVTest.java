package org.tespia;
import net.tespia.net.MapleCipher;
import net.tespia.net.MapleCustomEncryption;

import net.tespia.packets.Packets;
import net.tespia.common.HexTool;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class IVTest {

	private final static short MAPLE_VERSION = 62;

	public static byte ivSend[] = { 82, 48, 120, 20 };

	public static MapleCipher send_crypto;
	public static MapleCipher recvCypher;

	@BeforeClass
	public static void init() {
		
		send_crypto = new MapleCipher(ivSend, MAPLE_VERSION);
		recvCypher = new MapleCipher(ivSend, MAPLE_VERSION);

	}

	@Test
	public void testIv() {

		System.out.println("Send IV: " + HexTool.toString(send_crypto.getIv()));
		System.out.println("Recv IV: " + HexTool.toString(recvCypher.getIv()));

		byte inputPacket[] = Packets.getPong().getData();
		System.out.println("To be encrypted: " + HexTool.toString(inputPacket));
		
                byte encripted[] = inputPacket.clone();
                MapleCustomEncryption.encryptData(encripted);
		send_crypto.crypt(encripted);
		System.out.println("Encrupted: " + HexTool.toString(encripted));
		
                
                System.out.println("Crypt: " + HexTool.toString(encripted));
		byte unc[] = recvCypher.crypt(encripted);
		MapleCustomEncryption.decryptData(encripted);
		System.out.println("Decrypt: " + HexTool.toString(unc));// Should print
																// again the AA,
																// right?
																// However it
																// does not..

		System.out.println("IV: " + HexTool.toString(recvCypher.getIv()));
		System.out.println("IV: " + HexTool.toString(send_crypto.getIv()));
		
		byte [] result = Packets.getPong().getData();
		for(int i = 0; i < result.length;i++){
			Assert.assertTrue(result[i] == unc[i]);
		}

	}

}
