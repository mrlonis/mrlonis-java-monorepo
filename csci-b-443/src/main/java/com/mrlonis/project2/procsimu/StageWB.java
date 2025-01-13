package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.*;

/** Write Back */
public class StageWB extends Stage {

    /** Constructor for StageWB */
    public StageWB(Control control) {
        super(control);
        initWindow("Stage WB", 600, 320);
    }

    /**
     * Work Method Writes the result of the ARTH instr in the register, or or read the data from the cache (LOAD instr).
     */
    public void work() {
        long lngValue;
        long lngRegResult;
        long lngALUInstr;
        long lngMemInstr;

        print("---");

        // Latches read
        lngValue = control.stageMEM.latValue.read();
        lngRegResult = control.stageMEM.latRegResult.read();
        lngALUInstr = control.stageMEM.latALUInstr.read();
        lngMemInstr = control.stageMEM.latMemInstr.read();

        print("WB: Value: " + lngValue);
        print("WB: Reg Result: " + lngRegResult);

        // Register-register and control transfer
        switch ((int) lngALUInstr) {
            case Const.ALU_NOP:
                break;
            case Const.ALU_ADD: // register-register
            case Const.ALU_SUB: // register-register
            case Const.ALU_AND: // register-register
            case Const.ALU_OR: // register-register
            case Const.ALU_XOR: // register-register
            case Const.ALU_SLT: // register-register
            case Const.ALU_ADDI:
            case Const.ALU_ANDI:
            case Const.ALU_ORI:
            case Const.ALU_XORI:
            case Const.ALU_SLL:
            case Const.ALU_SRL:
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_BEQ: // control transfer
            case Const.ALU_BNE: // control transfer
            case Const.ALU_J: // control transfer
                // nothing to do
                break;
        }

        // Memory reference
        switch ((int) lngMemInstr) {
            case Const.MEM_NOP:
                // nothing to do
                break;
            case Const.MEM_LW: // Load
                long word = control.memory.latData.read();
                print("Memory read: " + word);
                control.register.write(lngRegResult, word);
                break;
            case Const.MEM_SW: // Store
                // nothing to do
                break;
        }
    }
}
