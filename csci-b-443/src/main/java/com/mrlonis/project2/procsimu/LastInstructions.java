package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.ClockChangeListener;
import com.mrlonis.project2.procsimu.comp.Const;

/** This class stores the two last instruction executed and the two last register number written. */
class LastInstructions implements ClockChangeListener {

    private static final int LAST_ARRAY_SIZE = 2;

    // Last instructions
    private long[] lastInstr; // Contains MIPS_*

    // Number of the register written by the last instructions
    // (last in index 0, before last in index 1, etc)
    private long[] lastNbReg;

    // Temporary variables to store the instr and the reg
    // until the clock cycle.
    private long tmpLastInstr;
    private long tmpLastNbReg;

    private boolean toUpdate;

    /** Last arrays initialization */
    public LastInstructions(Control control) {
        lastInstr = new long[LAST_ARRAY_SIZE];
        lastNbReg = new long[LAST_ARRAY_SIZE];
        clear();
        control.addClockChangeListener(this);
    }

    /**
     * Update the last arrays. They are updated at the next takt.
     *
     * @param lastB Last instruction executed
     * @param lastR Last register written
     */
    public void update(long lastB, long lastR) {
        tmpLastInstr = lastB;
        tmpLastNbReg = lastR;
        toUpdate = true;
    }

    /** Update the last arrays by adding info without data hazards. */
    public void update() {
        update(Const.MIPS_SW, -1L);
    }

    /** Clears the last arrays. */
    public void clear() {
        for (int i = 0; i < LAST_ARRAY_SIZE; i++) {
            lastInstr[i] = Const.MIPS_SW;
            lastNbReg[i] = -1L;
            // Because STORE has not data hazards
        }
        tmpLastInstr = Const.MIPS_SW;
        tmpLastNbReg = -1L;
        toUpdate = false;
    }

    /** The last arrays are really updated here */
    public void clockChanged() {
        if (toUpdate) {
            for (int i = lastInstr.length - 1; i > 0; i--) {
                lastInstr[i] = lastInstr[i - 1]; // Last Instruction
                lastNbReg[i] = lastNbReg[i - 1]; // Last register written
            }
            lastInstr[0] = tmpLastInstr;
            lastNbReg[0] = tmpLastNbReg;

            toUpdate = false;
            // print();
        }
    }

    /** Prints the 'last' arrays. */
    public void print() {
        System.out.println("        | Instr | Reg");
        System.out.println("Bef-last| " + Const.mipsToString((int) lastInstr[1]) + "  | " + lastNbReg[1]);
        System.out.println("Last    | " + Const.mipsToString((int) lastInstr[0]) + "  | " + lastNbReg[0]);
    }

    /** Returns the last instruction */
    public long getLastInstr() {
        return lastInstr[0];
    }

    /** Returns the instruction before last */
    public long getBeforeLastInstr() {
        return lastInstr[1];
    }

    /** Returns the last register written */
    public long getLastReg() {
        return lastNbReg[0];
    }

    /** Returns the register before last written */
    public long getBeforeLastReg() {
        return lastNbReg[1];
    }
}
