package com.mrlonis.project1.procsimu;

import com.mrlonis.project1.procsimu.comp.*;
import com.mrlonis.project1.procsimu.gui.Output;

/** A basic class to implement a stage. */
public abstract class Stage extends Output implements WorkListener {

    /** The control object */
    protected Control control;

    /** Method declaration. Empty method, should be implemented by subclasses. */
    public void work() {}

    /** Constructor declaration */
    public Stage(Control control) {
        this.control = control;
        control.addWorkListener(this);
    }

    /** Prints some text in the window */
    public void print(String str) {
        print(Const.OUTPUT_ALWAYS, str);
    }

    /**
     * Prints some text in the window. Actually, the message is only printed when <CODE>
     *  (level && Configuration.OUTPUT_LEVEL) != 0) </CODE>
     *
     * @param level Indicates when the message should be printed.
     * @param str Message to print
     */
    public void print(int level, String str) {
        if ((level & Configuration.OUTPUT_LEVEL) != 0) {
            super.print("[" + control.cycle + "] " + str);
        }
    }
}
