package org.tespia;

import org.junit.Test;

public class LearningBitManipulation {

    @Test
    public void testBits() {
        int num = 0xFFDDAAEE;
        int i = num & (0xFF << 24);
        System.out.println(printAsBinary(i));
        System.out.println(printAsHex(i));
        System.out.println("First byte:" + printAsHexByte(read_n_byte(num, 0)));
        System.out.println("Second byte:"+  printAsHexByte(read_n_byte(num, 1)));
        System.out.println("Third byte:" + printAsHexByte(read_n_byte(num, 2)));
        System.out.println("Forth byte:" + printAsHexByte(read_n_byte(num, 3)));

    }

    public int read_n_byte(int num, int bit){
        int i = bit * 8;
        return ((num & (0xFF << i)) >> i) & 0xFF;
    }

    String printAsBinary(int i) {
        return String.format("%32s", Integer.toBinaryString(i)).replaceAll(" ", "0");
    }
    String printAsHexByte(int i) {
        return String.format("%x", i).replaceAll(" ", "0");
    }


    String printAsHex(int i) {
        return String.format("%8x", i).replaceAll(" ", "0");
    }
}
