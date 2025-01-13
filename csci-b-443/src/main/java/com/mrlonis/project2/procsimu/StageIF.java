package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.*;

/** Instruction Fetch */
public class StageIF extends Stage {

    /** The PC */
    Latch latPC;

    /** This latch indicates to the IF stage if it should stall. Only used by the simple scalar. */
    Latch latStageIFStalled;

    // Next PC
    private long lngPC;

    /** Constructor for StageIF */
    public StageIF(Control control) {
        super(control);
        initWindow("Stage IF", 200, 120);

        latPC = new Latch(control);

        latStageIFStalled = new Latch(control);
    }

    /**
     * Reading the instruction, modifying the PC. Also branches if necessary, based on
     * control.stageEXE.latControlTransfer and control.stageEXE.latALUout
     */
    public void work() {
        print("PC=" + lngPC);

        // TODO Check latch to see if stage is stalled; if so, return early

        /* Beginning of added code */
        if (this.latStageIFStalled.read() == 1) {
            return;
        }
        /* End of added code */

        // Not stalled => Execute

        // If there is a jump/branch
        if (control.stageEXE.latControlTransfer.read() == 1) {
            // PC becomes calculated branch target
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
