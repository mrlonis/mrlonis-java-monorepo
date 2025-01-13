package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.*;

/** Instruction Fetch */
public class StageIF extends Stage {

    /** The PC */
    Latch latPC;

    // Next PC
    private long lngPC;

    /** Constructor for StageIF */
    public StageIF(Control control) {
        super(control);
        initWindow("Stage IF", 200, 120);

        latPC = new Latch(control);
    }

    /**
     * Reading the instruction, modifying the PC. Also branches if necessary, based on
     * control.stageEXE.latControlTransfer and control.stageEXE.latALUout
     */
    public void work() {
        print("PC=" + lngPC);

        // Check if there is a jump/branch
        // If so, update the PC to the branch target
        if (control.stageEXE.latControlTransfer.read() == 1) {
            // TODO read branch target from ALU output latch
            lngPC = control.stageEXE.latALUout.read();

            // We have a new PC, read PC in StageEXE
            print("Control transfer, new PC=" + lngPC);
        }

        // Update PC latch
        // Increment to next instruction
        latPC.write(lngPC); // Write new PC
        control.memory.readInstruction(lngPC); // Indicates to the memory the next instr to read
        lngPC += 4L; // Next PC
    }
}
