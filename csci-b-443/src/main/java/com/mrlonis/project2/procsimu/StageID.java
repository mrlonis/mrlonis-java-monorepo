package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.*;

/** Instruction Decode / Register Fetch */
public class StageID extends Stage {

    Latch latPC;
    Latch latReg1;
    Latch latReg2;
    Latch latImm;
    Latch latRegResult;
    Latch latALUInstr;
    Latch latMemInstr;

    // Last instructions / register written
    LastInstructions last;

    // The Result register
    long lngNbRegResult;

    // Value to save in case of a bubble
    private StageIDState stageState;

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

        stageState = new StageIDState();

        last = new LastInstructions(control); // for forwarding
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

        // Test if we had a jump and we should throw the instr away
        if (control.stageEXE.latControlTransfer.read() == 1) {
            print("Jump, throw the instr");

            // We try to avoid doing anything when there is a NOP. It is
            // detected here, at the beginning
            print("NOP");
            latALUInstr.write(Const.ALU_NOP);
            latMemInstr.write(Const.MEM_NOP);
            // We CLEAR the last instr/reg arrays
            last.clear();

            return;
        }

        // ************************
        // *** GETTING THE DATA ***
        // ************************

        // *** Stage stalled ***
        if (control.stageIF.latStageIFStalled.read() == 1) {
            // If the stage was stalled, we unstall it (max stall possible is one)
            // and we restore the state of the stage before the bubble
            // (state to execute)
            print(Const.OUTPUT_FORW, "Unstall the stage");
            // Unstall the stage
            control.stageIF.latStageIFStalled.write(0L);
            // Search the data to execute
            lngInstr = stageState.lngInstr;
            lngPC = stageState.lngPC;
            lngOp = stageState.lngOp;
            lngNbReg1 = stageState.lngNbReg1;
            lngNbReg2 = stageState.lngNbReg2;
        }
        // *** Get the data from the normal place ***
        else {
            // Reading the instr to decode
            lngInstr = control.memory.latInstr.read();
            // We got the instr to decode!
            lngPC = control.stageIF.latPC.read();
            lngOp = Conversion.extractOp(lngInstr);
            lngNbReg1 = Conversion.extractR1(lngInstr);
            lngNbReg2 = Conversion.extractR2(lngInstr);
        }

        print(Const.toString(lngInstr));
        print("PC: " + lngPC);

        // ***********
        // *** NOP ***
        // ***********
        if (lngInstr == Const.INSTR_NOP) {
            // We try to avoid doing anything when there is a NOP and it is
            // detected here, at the begining
            print("NOP");
            // Update the last arrays
            last.update();
            latPC.write(lngPC);
            latALUInstr.write(Const.ALU_NOP);
            latMemInstr.write(Const.MEM_NOP);
            return;
        }

        // *****************
        // *** Not a NOP ***
        // *****************
        print("Nb Reg1:  " + lngNbReg1 + "; Reg2:   " + lngNbReg2);
        print(Const.OUTPUT_FORW, "Last Reg: " + last.getLastReg() + "; BF Reg: " + last.getBeforeLastReg());

        // for storing result of data hazard tests
        long lngTmp1, lngTmp2;

        // We choose to manage the data hazards for each type of instr.
        switch ((int) lngOp) {
            case Const.MIPS_ARTH:
                // ** register-register **
                print("MIPS_ARTH");
                lngNbRegResult = Conversion.extractR3(lngInstr);

                // Test if data hazards for the register 1 or 2
                lngTmp1 = StageID.testDataHazards(lngNbReg1, this);
                lngTmp2 = StageID.testDataHazards(lngNbReg2, this);
                if ((lngTmp1 == -1) || (lngTmp2 == -1)) {
                    // Insert a bubble
                    latALUInstr.write(Const.ALU_NOP); // Insert NOP
                    latMemInstr.write(Const.MEM_NOP);
                    last.update(); // Update the last arrays
                    control.stageIF.latStageIFStalled.write(1L); // Stall the IF stage
                    stageState.store(lngInstr, lngPC, lngOp, lngNbReg1, lngNbReg2);
                    return;
                }
                lngNbReg1 = lngTmp1;
                lngNbReg2 = lngTmp2;

                // Instruction Decode
                long lngOpx = Conversion.extractOpx(lngInstr);
                print("Opx: " + lngOpx);
                switch ((int) lngOpx) {
                    case Const.EXT_ADD:
                        lngALUInstr = Const.ALU_ADD;
                        break;
                    case Const.EXT_SUB:
                        lngALUInstr = Const.ALU_SUB;
                        break;
                    case Const.EXT_AND:
                        lngALUInstr = Const.ALU_AND;
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

                // Check for data hazards for register 2
                lngTmp2 = StageID.testDataHazards(lngNbReg2, this);
                if (lngTmp2 == -1) {
                    // Insert a bubble
                    latALUInstr.write(Const.ALU_NOP); // Insert NOP
                    latMemInstr.write(Const.MEM_NOP);
                    last.update(); // Update the last arrays
                    control.stageIF.latStageIFStalled.write(1L); // Stall the IF stage
                    stageState.store(lngInstr, lngPC, lngOp, lngNbReg1, lngNbReg2);
                    return;
                }
                lngNbReg2 = lngTmp2;

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));
                lngMemInstr = Const.MEM_SW;

                break; // -- END case MIPS_SW

            case Const.MIPS_BNE:
            case Const.MIPS_BEQ:
                // ** BRANCH **

                // TODO Check for data hazards for register 1 or 2
                // if stall is needed, return early

                /* Beginning of added code */
                lngTmp1 = testDataHazards(lngNbReg1, this);
                lngTmp2 = testDataHazards(lngNbReg2, this);

                if (lngTmp1 == -1 || lngTmp2 == -1) {
                    latALUInstr.write(Const.ALU_NOP);
                    latMemInstr.write(Const.MEM_NOP);
                    last.update();
                    control.stageIF.latStageIFStalled.write(1L);
                    stageState.store(lngInstr, lngPC, lngOp, lngNbReg1, lngNbReg2);
                    return;
                }
                /* End of added code */

                lngNbReg1 = lngTmp1;
                lngNbReg2 = lngTmp2;

                // Instruction Decode
                lngImm = (short) (Conversion.extractImm(lngInstr));

                switch ((int) lngOp) {
                    case Const.MIPS_BEQ:
                        print("MIPS_BEQ");
                        lngALUInstr = Const.ALU_BEQ;
                    case Const.MIPS_BNE:
                        print("MIPS_BNE");
                        lngALUInstr = Const.ALU_BNE;
                }

                break; // -- END case MIPS_BEQ/BNE

            case Const.MIPS_J:
                // ** JUMP **
                print("MIPS_J");

                // No data hazards possible on a JUMP instr

                // Instruction Decode
                // Bits 0 - 25 extrahieren und als Byte-Wert ausgeben
                lngImm = Conversion.extractLongImm(lngInstr);
                lngALUInstr = Const.ALU_J;

                break; // -- END case MIPS_J

            case Const.MIPS_ADDI:
            case Const.MIPS_ANDI:
            case Const.MIPS_ORI:
            case Const.MIPS_XORI:
                lngNbRegResult = Conversion.extractR3(lngInstr);

                // TODO Check for data hazards for register 2 if stall is needed, return early

                /* Beginning of added code */
                lngTmp2 = testDataHazards(lngNbReg2, this);
                if (lngTmp2 == -1) {
                    latALUInstr.write(Const.ALU_NOP);
                    latMemInstr.write(Const.MEM_NOP);
                    last.update();
                    control.stageIF.latStageIFStalled.write(1L);
                    stageState.store(lngInstr, lngPC, lngOp, lngNbReg1, lngNbReg2);
                    return;
                }
                /* End of added code */

                /*
                 * This was the original code provided to us for the assignment however, the XORI command asks for us to
                 * check register 2. Based off the MIPS_SW case, I am changing this code from lngNbReg1 = lngTmp1 to
                 * lngNbReg2 = lngTmp2. I don't know if the TO DO comment wrongly stated register 2 or not but I'm going
                 * to assume it's correct and change the code here.
                 *
                 * I don't know if this is correct but based off of the MIPS_SW I believe this is in error.
                 */
                // lngNbReg1 = lngTmp1;
                lngNbReg2 = lngTmp2;

                switch ((int) lngOp) {
                    case Const.MIPS_ADDI:
                        lngALUInstr = Const.ALU_ADDI;
                        print("MIPS_ADDI");
                        break;
                    case Const.MIPS_ANDI:
                        print("MIPS_ANDI");
                        lngALUInstr = Const.ALU_ANDI;
                        break;
                    case Const.MIPS_ORI:
                        print("MIPS_ORI");
                        lngALUInstr = Const.ALU_ORI;
                        break;
                    case Const.MIPS_XORI:
                        print("MIPS_XORI");
                        lngALUInstr = Const.ALU_XORI;
                        break;
                }

                lngImm = Conversion.extractImm(lngInstr);
                lngNbRegResult = Conversion.extractR2(lngInstr);
                break;
        } // *** END switch(lngOp) ***

        // Update the last arrays
        last.update(lngOp, lngNbRegResult);

        latPC.write(lngPC);
        latReg1.write(lngNbReg1);
        latReg2.write(lngNbReg2);
        latImm.write(lngImm);
        latRegResult.write(lngNbRegResult);
        latALUInstr.write(lngALUInstr);
        latMemInstr.write(lngMemInstr);
    }

    /**
     * Check for data hazards.
     *
     * @param nbReg The number of the register to test.
     * @param s The stage to be tested
     * @return The new number for the register (eventually the forwarding code) or -1 if the stage should be stalled.
     */
    public static long testDataHazards(long nbReg, Stage s) {
        // Since register 0 has a particular use, there can never be any forwarding on it
        if (nbReg == 0L) {
            return nbReg;
        }

        // Dep on the last instr of the 1st pipeline (type 1)
        else if (nbReg == s.control.stageID.last.getLastReg()) {
            s.print(Const.OUTPUT_FORW, "DH, last instr");
            if (s.control.stageID.last.getLastInstr() == Const.MIPS_LW) {
                // then wait until the load is completed
                s.print(Const.OUTPUT_FORW, "Last instr was Load => 1 Bubble");

                // TODO If the last instruction was LOAD, indicate a stall

                /* Beginning of added code */
                return (long) -1;
                /* End of added code */

            } else {
                s.print(Const.OUTPUT_FORW, "Forwarding from EXE");

                // TODO Not a load, so forward from the EXE stage

                /* Beginning of added code */
                nbReg = Const.FORWARDING_STAGE_EXE;
                /* End of added code */

            }
        }

        // Dep on instr before last of the 1st pipeline (type 2)
        else if (nbReg == s.control.stageID.last.getBeforeLastReg()) {
            s.print(Const.OUTPUT_FORW, "DH, before last instr");
            // If the last instruction was LOAD
            if (s.control.stageID.last.getBeforeLastInstr() == Const.MIPS_LW) {
                // then wait until the load is completed
                s.print(Const.OUTPUT_FORW, "Instr before last was Load, forw from RAM");
                nbReg = Const.FORWARDING_STAGE_RAM;
            } else {
                s.print(Const.OUTPUT_FORW, "Forwarding from MEM");

                // TODO Not a load, so forwading from the MEM stage

                /* Beginning of added code */
                nbReg = Const.FORWARDING_STAGE_MEM;
                /* End of added code */

            }
        }

        return nbReg;
    }

    /**
     * A wrapping class to save the state of the stage in case of a bubble.
     *
     * @author Vincent Oberle
     */
    private class StageIDState {
        long lngInstr;
        long lngPC;
        long lngOp;
        long lngNbReg1;
        long lngNbReg2;

        /**
         * Convenience method to store the state. Usage:
         *
         * <PRE>
         * stageState.store (lngInstr, lngPC, lngOp, lngNbReg1, lngNbReg2);
         * </PRE>
         */
        void store(long lngInstr, long lngPC, long lngOp, long lngNbReg1, long lngNbReg2) {
            this.lngInstr = lngInstr;
            this.lngPC = lngPC;
            this.lngOp = lngOp;
            this.lngNbReg1 = lngNbReg1;
            this.lngNbReg2 = lngNbReg2;
        }
    } // class StageIDState
}
