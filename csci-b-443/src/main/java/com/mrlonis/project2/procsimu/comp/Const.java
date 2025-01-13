package com.mrlonis.project2.procsimu.comp;

/**
 * Constants for the processor simulation.
 *
 * @author Modified by Vincent Oberle.
 */
public class Const {

    // *** INSTRUCTIONS ***

    // The mem value used to represent a NOP
    public static final int INSTR_NOP = 32;

    // MIPS primary instructions
    public static final int MIPS_ARTH = 0;
    public static final int MIPS_LW = 35;
    public static final int MIPS_SW = 43;
    public static final int MIPS_BEQ = 4;
    public static final int MIPS_BNE = 5;
    public static final int MIPS_J = 2;
    public static final int MIPS_ADDI = 8;
    public static final int MIPS_ANDI = 12;
    public static final int MIPS_ORI = 13;
    public static final int MIPS_XORI = 14;

    // MIPS extended instructions
    public static final int EXT_AND = 36;
    public static final int EXT_ADD = 32;
    public static final int EXT_SUB = 34;
    public static final int EXT_OR = 37;
    public static final int EXT_XOR = 38;
    public static final int EXT_SLT = 42;
    public static final int EXT_SLL = 0;
    public static final int EXT_SRL = 2;

    // ALU instructions
    public static final int ALU_NOP = 0;
    public static final int ALU_ADD = 1;
    public static final int ALU_SUB = 2;
    public static final int ALU_AND = 3;
    public static final int ALU_OR = 4;
    public static final int ALU_XOR = 5;
    public static final int ALU_BEQ = 6;
    public static final int ALU_SLT = 7;
    public static final int ALU_J = 8;
    public static final int ALU_ADDI = 9;
    public static final int ALU_ANDI = 10;
    public static final int ALU_ORI = 11;
    public static final int ALU_XORI = 12;
    public static final int ALU_SLL = 13;
    public static final int ALU_SRL = 14;
    public static final int ALU_BNE = 15;

    // MEM instructions
    public static final int MEM_NOP = 0;
    public static final int MEM_LW = 1;
    public static final int MEM_SW = 2;

    // *** STAGES ***

    public static final int STAGE_IF = 0;
    public static final int STAGE_ID = 1;
    public static final int STAGE_EXE = 2;
    public static final int STAGE_MEM = 3;
    public static final int STAGE_WB = 4;

    // *** FORWARDING ***

    public static final long FORWARDING_STAGE_EXE = 100;
    public static final long FORWARDING_STAGE_MEM = 200;
    public static final long FORWARDING_STAGE_EXE2 = 300;
    public static final long FORWARDING_STAGE_MEM2 = 400;
    public static final long FORWARDING_STAGE_RAM = 500;

    // *** MEMORY ***

    // Address value in latches?
    public static final int ADDR_NULL = -1;

    /** Constants indicating when to print a conditional message. */
    public static final int OUTPUT_ALWAYS = 0xFFFFFFFF; // Always print

    public static final int OUTPUT_FORW = 0x1; // Message linked to forwarding
    public static final int OUTPUT_CACHE = 0x2; // Message linked to cache
    public static final int OUTPUT_BP = 0x4; // Message linked to branch prediction
    public static final int OUTPUT_MULTI = 0x8; // Message linked to multi scalar

    // *** UTIL METHODS ***

    /** Returns a String representation of the Instruction */
    public static String toString(long instr) {
        String st = "";
        if (instr == INSTR_NOP) {
            return "NOP";
        }
        long lngOp = Conversion.extractOp(instr);
        switch ((int) lngOp) {
            case Const.MIPS_ARTH:
                st += "ARTH";
                long lngOpx = Conversion.extractOpx(instr);
                switch ((int) lngOpx) {
                    case Const.EXT_ADD:
                        st += " ADD";
                        break;
                    case Const.EXT_SUB:
                        st += " SUB";
                        break;
                    case Const.EXT_AND:
                        st += " AND";
                        break;
                    case Const.EXT_OR:
                        st += " OR ";
                        break;
                    case Const.EXT_SLT:
                        st += " SLT";
                        break;
                    default:
                        st += " NOP";
                        break;
                }
                st += " R1 " + Conversion.extractR1(instr);
                st += " R2 " + Conversion.extractR2(instr);
                break;
            case Const.MIPS_LW:
                st += "LW";
                st += " Imm " + (short) (Conversion.extractImm(instr));
                st += " RRes " + Conversion.extractR2(instr);
                break;
            case Const.MIPS_SW:
                st += "SW";
                st += " Imm " + (short) (Conversion.extractImm(instr));
                break;
            case Const.MIPS_BEQ:
                st += "BEQ";
                st += " Imm " + (short) (Conversion.extractImm(instr));
                break;
            case Const.MIPS_J:
                st += "J";
                st += " Imm " + Conversion.extractLongImm(instr);
                break;
        }
        return st;
    }

    /** Converts a MIPS constants to a string */
    public static String mipsToString(int mips) {
        switch (mips) {
            case MIPS_ARTH:
                return "ARTH";
            case MIPS_LW:
                return " LW ";
            case MIPS_SW:
                return " SW ";
            case MIPS_BEQ:
                return "BEQ ";
            case MIPS_J:
                return " J  ";
            default:
                return Integer.toString(mips);
        }
    }
}
