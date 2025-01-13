package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.*;

/** Instruction Decode / Register Fetch */
public class StageID extends Stage {

    Latch latPC;
    Latch latReg1;
    Latch latReg2;
    Latch latImm;
    Latch latRegResult;
    Latch latALUInstr;
    Latch latMemInstr;

    // The Result register
    long lngNbRegResult;

    /** Constructor for StageID */
    public StageID(Control control) {
        super(control);
        initWindow("Stage ID", 400, 120);

        latPC = new Latch(control);
        latReg1 = new Latch(control);
        latReg2 = new Latch(control);
        latImm = new Latch(control);
        latRegResult = new Latch(control);
        latALUInstr = new Latch(control);
        latMemInstr = new Latch(control);

        lngNbRegResult = 0L;
    }

    /** Work method for the IF stage. */
    public void work() {
        // In
        long lngInstr;
        long lngPC;
        long lngOp;
        long lngNbReg1;
        long lngNbReg2;

        // Out
        long lngImm = 0;
        lngNbRegResult = 0;

        long lngALUInstr = Const.ALU_NOP;
        long lngMemInstr = Const.MEM_NOP;

        print("---");

        // ************************
        // *** GETTING THE DATA ***
        // ************************

        // Read the instr to decode
        lngInstr = control.memory.latInstr.read();

        lngPC = control.stageIF.latPC.read();
        lngOp = Conversion.extractOp(lngInstr);
        lngNbReg1 = Conversion.extractR1(lngInstr);
        lngNbReg2 = Conversion.extractR2(lngInstr);

        print(Const.toString(lngInstr));
        print("PC: " + lngPC);

        // ***********
        // *** NOP ***
        // ***********
        if (lngInstr == Const.INSTR_NOP) {
            // We try to avoid doing anything when there is a NOP
            print("NOP");
            latPC.write(lngPC);
            latALUInstr.write(Const.ALU_NOP);
            latMemInstr.write(Const.MEM_NOP);
            return;
        }

        // *****************
        // *** Not a NOP ***
        // *****************
        print("Nb Reg1:  " + lngNbReg1 + "; Reg2:   " + lngNbReg2);

        long lngTmp1, lngTmp2;

        // We choose to manage the data hazards for each type of instr.
        switch ((int) lngOp) {
            case Const.MIPS_ARTH:
                // ** register-register **
                print("MIPS_ARTH");
                lngNbRegResult = Conversion.extractR3(lngInstr);

                // Instruction Decode
                long lngOpx = Conversion.extractOpx(lngInstr);
                print("Opx: " + lngOpx);
                switch ((int) lngOpx) {
                    case Const.EXT_AND:
                        lngALUInstr = Const.ALU_AND;
                        break;
                        // TODO set ALU opcode for ADD, SUB, OR, XOR, SLT
                    case Const.EXT_ADD:
                        lngALUInstr = Const.ALU_ADD;
                        break;
                    case Const.EXT_SUB:
                        lngALUInstr = Const.ALU_SUB;
                        break;
                    case Const.EXT_OR:
                        lngALUInstr = Const.ALU_OR;
                        break;
                    case Const.EXT_XOR:
                        lngALUInstr = Const.ALU_XOR;
                        break;
                    case Const.EXT_SLT:
                        lngALUInstr = Const.ALU_SLT;
                        break;
                    case Const.EXT_SLL:
                        lngALUInstr = Const.ALU_SLL;
                        lngImm = Conversion.extractShAmt(lngInstr);
                        break;
                    case Const.EXT_SRL:
                        // TODO ALU opcode for SRL
                        lngALUInstr = Const.ALU_SRL;
                        lngImm = Conversion.extractShAmt(lngInstr);
                        break;
                    default:
                        lngALUInstr = Const.ALU_NOP;
                        break;
                }

                break; // -- END case MIPS_ARTH --
            case Const.MIPS_LW:
                // ** LOAD **
                print("MIPS_LW");

                // No data hazards possible on a LOAD instr

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));
                lngNbRegResult = Conversion.extractR2(lngInstr);
                lngMemInstr = Const.MEM_LW;

                break; // -- END case MIPS_LW --
            case Const.MIPS_SW:
                // ** STORE **
                print("MIPS_SW");

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));
                lngMemInstr = Const.MEM_SW;

                break; // -- END case MIPS_SW
            case Const.MIPS_BEQ:
                // ** BRANCH **
                print("MIPS_BEQ");

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));
                lngALUInstr = Const.ALU_BEQ;

                break; // -- END case MIPS_BEQ
            case Const.MIPS_BNE:
                // TODO BNE

                // ** BRANCH **
                print("MIPS_BNE");

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));
                lngALUInstr = Const.ALU_BNE;

                break; // -- END case MIPS_BNE
            case Const.MIPS_J:
                // ** JUMP **
                print("MIPS_J");

                // No data hazards possible on a JUMP instr

                // Instruction Decode
                // Bits 0 - 25 extrahieren und als Byte-Wert ausgeben
                lngImm = Conversion.extractLongImm(lngInstr);
                lngALUInstr = Const.ALU_J;

                break; // -- END case MIPS_J
            case Const.MIPS_ANDI:
                print("MIPS_ANDI");
                lngImm = Conversion.extractImm(lngInstr);
                lngNbRegResult = Conversion.extractR2(lngInstr);
                lngALUInstr = Const.ALU_ANDI;
                break;
                // TODO ADDI, ORI, XORI
            case Const.MIPS_ADDI:
                print("MIPS_ADDI");
                lngImm = Conversion.extractImm(lngInstr);
                lngNbRegResult = Conversion.extractR2(lngInstr);
                lngALUInstr = Const.ALU_ADDI;
                break;
            case Const.MIPS_ORI:
                print("MIPS_ORI");
                lngImm = Conversion.extractImm(lngInstr);
                lngNbRegResult = Conversion.extractR2(lngInstr);
                lngALUInstr = Const.ALU_ORI;
                break;
            case Const.MIPS_XORI:
                print("MIPS_XORI");
                lngImm = Conversion.extractImm(lngInstr);
                lngNbRegResult = Conversion.extractR2(lngInstr);
                lngALUInstr = Const.ALU_XORI;
                break;
        } // *** END switch(lngOp) ***

        latPC.write(lngPC);
        latReg1.write(lngNbReg1);
        latReg2.write(lngNbReg2);
        latImm.write(lngImm);
        latRegResult.write(lngNbRegResult);
        latALUInstr.write(lngALUInstr);
        latMemInstr.write(lngMemInstr);
    }
}
