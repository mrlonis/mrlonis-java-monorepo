package com.mrlonis.project1.procsimu.comp;

/** Extraction of the instructions and other information from an encoded instruction. */
public class Conversion {

    /** Extract the Op from the value. */
    public static byte extractOp(long value) {
        // Extract bits 26 - 31
        long result = value & 0xfc000000; // Bits 26-31
        result = result >> 26;
        return (byte) result;
    }

    /** Extract the Opx from the value. */
    public static byte extractOpx(long value) {
        // Extract bits 0 - 5
        long result = value & 0x0000003f; // Bits 0-5
        return (byte) result;
    }

    /** Extract the R1 number from the value. */
    public static byte extractR1(long value) {
        // Extract bits 21 - 25
        long result = value & 0x03e00000; // Bits 21-25
        result = result >> 21;
        return (byte) result;
    }

    /** Extract the R2 number from the value. */
    public static byte extractR2(long value) {
        // Extract bits 16 - 20
        long result = value & 0x001f0000; // Bits 16-20
        result = result >> 16;
        return (byte) result;
    }

    /** Extract the R3 number from the value. */
    public static byte extractR3(long value) {
        // Extract bits 11 - 15
        long result = value & 0x0000f800; // Bits 11-15
        result = result >> 11;
        return (byte) result;
    }

    /** Extract the immediate value from the value. */
    public static long extractImm(long value) {
        // Extract bits 0 - 15
        long result = value & 0x0000ffff; // Bits 0-15
        return result;
    }

    /** Extract the long immediate value from the value. */
    public static long extractLongImm(long value) {
        // Extract bits 0 - 25
        long result = value & 0x03ffffff; // Bits 0-25
        return result;
    }

    /** Extract the shift amount from the value. */
    public static long extractShAmt(long value) {
        // Bits 6-10
        return (value >> 6) & 0x1f;
    }
}
