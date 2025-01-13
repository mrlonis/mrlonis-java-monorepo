package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.*;

/** Execution / Address Calculation. */
public class StageEXE extends Stage {

    Latch latALUout;
    Latch latStoreVal;
    Latch latRegResult;
    Latch latALUInstr;
    Latch latMemInstr;

    /** Control transfer according to the speculation */
    Latch latControlTransfer;

    /** Counts the number of instruction executed (in order to calculate the ICP) */
    int nbInstrExecuted;

    /** Constructor for StageEXE */
    public StageEXE(Control control) {
        super(control);
        initWindow("Stage EXE", 200, 320);

        latALUout = new Latch(control);
        latStoreVal = new Latch(control);
        latRegResult = new Latch(control);
        latALUInstr = new Latch(control);
        latMemInstr = new Latch(control);

        latControlTransfer = new Latch(control);

        nbInstrExecuted = 0;
    }

    /**
     * The work method for EXE. - Tests if there is a control transfer - Manages forwarding - Execute (ARTH, MEM, or
     * Control Transfer)
     */
    public void work() {
        long lngPC;
        long lngNbReg1; // Number of the register
        long lngNbReg2; // -
        long lngReg1; // Value of the register
        long lngReg2; // -
        long lngImm;
        long lngNbRegResult;
        long lngALUout = 0;
        long lngALUInstr;
        long lngMemInstr;
        long lngStoreVal = 0;
        // Control transfer according to the speculation
        long lngControlTransferEffective = 0;

        print("---");

        // *** Read the latches ***

        lngPC = control.stageID.latPC.read();
        lngImm = control.stageID.latImm.read();
        lngNbRegResult = control.stageID.latRegResult.read();
        lngALUInstr = control.stageID.latALUInstr.read();
        lngMemInstr = control.stageID.latMemInstr.read();
        // Get the number of the register to read (or the forwarding info)
        lngNbReg1 = control.stageID.latReg1.read();
        lngNbReg2 = control.stageID.latReg2.read();

        // Test if we had a jump and we should throw the instr away
        if (control.stageEXE.latControlTransfer.read() == 1L) {
            print("Jump, throw the instr");
            latALUInstr.write(Const.ALU_NOP);
            latMemInstr.write(Const.ALU_NOP);
            control.stageEXE.latControlTransfer.write(0L);
            return;
        }

        print("PC: " + lngPC);
        print("NbReg1: " + lngNbReg1 + ", NbReg2: " + lngNbReg2);

        // *** Manage forwarding ***

        print("Forwarding: test reg 1");
        lngReg1 = StageEXE.getRegisterValue(lngNbReg1, this);

        print("Forwarding: test reg 2");
        lngReg2 = StageEXE.getRegisterValue(lngNbReg2, this);

        print("Reg1: " + lngReg1 + ", Reg2: " + lngReg2);
        print("Imm: " + lngImm + ", NbRegResult: " + lngNbRegResult);

        // *** ALU Execution ***

        // -- ALU instruction --
        // print("ALU Instr: " + lngALUInstr);
        switch ((int) lngALUInstr) {
            case Const.ALU_NOP:
                print("ALU NOP");
                // nothing to do
                break;
            case Const.ALU_ADD: // register-register
                print("ALU ADD");
                lngALUout = lngReg1 + lngReg2;
                break;
            case Const.ALU_SUB: // register-register
                print("ALU SUB");
                lngALUout = lngReg1 - lngReg2;
                break;
            case Const.ALU_AND: // register-register
                print("ALU AND");
                lngALUout = lngReg1 & lngReg2;
                break;
            case Const.ALU_OR: // register-register
                print("ALU OR");
                lngALUout = lngReg1 | lngReg2;
                break;
            case Const.ALU_XOR: // register-register
                print("ALU XOR");
                lngALUout = lngReg1 ^ lngReg2;
                break;
            case Const.ALU_SLT: // register-register
                print("ALU SLT");
                if (lngReg1 < lngReg2) {
                    lngALUout = 1L;
                } else {
                    lngALUout = 0L;
                }
                break;

            case Const.ALU_ADDI:
                print("ALU ADDI");
                lngALUout = lngReg1 + lngImm;
                break;
            case Const.ALU_ANDI:
                print("ALU ANDI");
                lngALUout = lngReg1 & lngImm;
                break;
            case Const.ALU_ORI:
                print("ALU ORI");
                lngALUout = lngReg1 | lngImm;
                break;
            case Const.ALU_XORI:
                print("ALU XORI");
                lngALUout = lngReg1 ^ lngImm;
                break;

            case Const.ALU_SLL:
                print("ALU SLL");
                lngALUout = lngReg2 << lngImm;
                break;
            case Const.ALU_SRL:
                print("ALU SRL");
                lngALUout = lngReg2 >>> lngImm;
                break;

                // -- CONTROL TRANSFER --
            case Const.ALU_BEQ: // control transfer
                print("ALU BEQ");
                if (lngReg1 == lngReg2) {
                    print("reg1=reg2, branch, ALUout: " + lngALUout);
                    lngControlTransferEffective = 1;
                    lngALUout = lngImm;
                } else {
                    // If we have to go back, give the address of the instr following
                    // the BEQ as destination address
                    lngALUout = lngPC + 4L;
                }
                break;
            case Const.ALU_BNE:
                print("ALU BNE");
                if (lngReg1 != lngReg2) {
                    print("reg1!=reg2, branch, ALUout: " + lngALUout);
                    lngControlTransferEffective = 1;
                    lngALUout = lngImm;
                } else {
                    // If we have to go back, give the address of the instr following
                    // the BNE as destination address
                    lngALUout = lngPC + 4L;
                }
                break;
            case Const.ALU_J: // control transfer
                print("ALU J");
                lngALUout = lngImm;
                lngControlTransferEffective = 1;
                break;
        } // end switch (lngALUInstr)

        print("ALU out: " + lngALUout);

        // -- Memory reference --
        // print("MEM Instr: " + lngMemInstr);
        switch ((int) lngMemInstr) {
                // NB : The MIPS instr. LOAD or STORE have an absolute value (in reg 1)
                //      + an immediate value (offset) to form the address to read
                //      or to write. But this isn't implemented here and the absolute
                //      part of the address is always 0.
                //   => Not Data hazards on LOAD, only for reg2 on STORE
            case Const.MEM_NOP:
                print("MEM NOP");
                // nothing to do
                break;
            case Const.MEM_LW:
                print("MEM LW");
                lngALUout = lngImm; // lngReg1 + lngImm;
                break;
            case Const.MEM_SW:
                print("MEM SW");
                lngALUout = lngImm; // lngReg1 + lngImm;
                lngStoreVal = lngReg2;
                break;
        }

        print("ALU Out: " + lngALUout);

        // If we are here, an instruction may have been executed
        if ((lngALUInstr != Const.ALU_NOP) || (lngMemInstr != Const.MEM_NOP)) {
            nbInstrExecuted++;
        }

        latALUout.write(lngALUout);
        latStoreVal.write(lngStoreVal);
        latRegResult.write(lngNbRegResult);

        latALUInstr.write(lngALUInstr);
        latMemInstr.write(lngMemInstr);

        // Update the branch prediction latch
        latControlTransfer.write(lngControlTransferEffective);
    }

    /**
     * Manage forwarding. Returns the value of the register after testing the forwarding situation (ie the register
     * number). Superscalar implemented.
     *
     * @param lngNbReg The number of the register to test.
     * @param s The stage to be tested.
     * @return The register VALUE.
     */
    static long getRegisterValue(long lngNbReg, Stage s) {
        long lngReg;
        if (lngNbReg == Const.FORWARDING_STAGE_EXE) {
            s.print("Forward from EXE");
            lngReg = s.control.stageEXE.latALUout.read();
        } else if (lngNbReg == Const.FORWARDING_STAGE_MEM) {
            s.print("Forward from MEM");

            // TODO implement forwarding from MEM stage

            /* Beginning of added code */
            lngReg = s.control.stageMEM.latValue.read();
            /* End of added code */

        } else if (lngNbReg == Const.FORWARDING_STAGE_RAM) {
            s.print("Forward from RAM");
            lngReg = s.control.memory.latData.read();
        } else { // No forwarding
            lngReg = s.control.register.read(lngNbReg);
        }
        return lngReg;
    }
}
