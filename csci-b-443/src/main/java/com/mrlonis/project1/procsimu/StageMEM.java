package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.*;

/** Memory Access */
public class StageMEM extends Stage {

    Latch latRegResult;
    Latch latValue;
    Latch latALUInstr;
    Latch latMemInstr;

    /** Constructor for StageMEM */
    public StageMEM(Control control) {
        super(control);
        initWindow("Stage MEM", 400, 320);

        latRegResult = new Latch(control);
        latValue = new Latch(control);
        latALUInstr = new Latch(control);
        latMemInstr = new Latch(control);
    }

    /** Reads or write data if there is a MEM instr. */
    public void work() {
        long lngALUout;
        long lngStoreVal;
        long lngRegResult;
        long lngALUInstr;
        long lngMemInstr;

        // Instr read
        lngALUout = control.stageEXE.latALUout.read();
        lngStoreVal = control.stageEXE.latStoreVal.read();
        lngRegResult = control.stageEXE.latRegResult.read();
        lngALUInstr = control.stageEXE.latALUInstr.read();
        lngMemInstr = control.stageEXE.latMemInstr.read();

        // Register-register and control transfer
        // print("ALU Instr: " + lngALUInstr);
        switch ((int) lngALUInstr) {
            case Const.ALU_NOP:
            case Const.ALU_ADD: // register-register
            case Const.ALU_SUB: // register-register
            case Const.ALU_AND: // register-register
            case Const.ALU_OR: // register-register
            case Const.ALU_SLT: // register-register
            case Const.ALU_ADDI: // register-immediate
            case Const.ALU_ANDI: // register-immediate
            case Const.ALU_ORI: // register-immediate
            case Const.ALU_XORI: // register-immediate
            case Const.ALU_SLL: // register-immediate
            case Const.ALU_SRL: // register-immediate
                // nothing to do
                break;
            case Const.ALU_BEQ: // control transfer
            case Const.ALU_BNE: // control transfer
            case Const.ALU_J: // control transfer
                // nothing to do
                break;
        }

        // Memory reference
        // print("MEM Instr: " + lngMemInstr);
        switch ((int) lngMemInstr) {
            case Const.MEM_NOP:
                // nothing to do
                break;
            case Const.MEM_LW: // Load
                print("Load at: " + lngALUout);

                // TODO tell memory which address to read
                control.memory.readData(lngALUout);

                break;
            case Const.MEM_SW: // Store
                print("Store " + lngStoreVal + " at " + lngALUout);
                control.memory.write(lngALUout, lngStoreVal);
                break;
        }

        // Update latches
        latValue.write(lngALUout);
        latRegResult.write(lngRegResult);
        latALUInstr.write(lngALUInstr);
        latMemInstr.write(lngMemInstr);
    }
}
