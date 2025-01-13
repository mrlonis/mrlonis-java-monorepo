package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.*;

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
            case Const.ALU_AND: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
                // TODO writeback for ADD, SUB, OR, XOR, SLT, ADDI, ANDI, ORI, XORI, SLL, SRL
            case Const.ALU_ADD: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_SUB: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_OR: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_XOR: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_SLT: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_ADDI: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_ANDI: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_ORI: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_XORI: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_SLL: // register-register
                control.register.write(lngRegResult, lngValue);
                break;
            case Const.ALU_SRL: // register-register
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
            case Const.MEM_SW: // Store
                // nothing to do
                break;
            case Const.MEM_LW: // Load
                // TODO read from memory data latch
                long word = control.memory.latData.read();
                print("Memory read: " + word);
                // TODO write to result register
                control.register.write(lngRegResult, word);
                break;
        }
    }
}
