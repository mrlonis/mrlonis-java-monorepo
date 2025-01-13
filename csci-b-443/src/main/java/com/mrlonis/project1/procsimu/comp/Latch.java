package com.mrlonis.project1.procsimu.comp;

import com.mrlonis.project1.procsimu.Control;

/** A 32-bit latch. */
public class Latch implements ClockChangeListener {

    private long value;
    private long tmpValue;

    public Latch(Control control) {
        control.addClockChangeListener(this);
    }

    /** Update the latch */
    public void clockChanged() {
        value = tmpValue;
    }

    /** Returns the value of the latch. */
    public long read() {
        return value;
    }

    /** Writes the value in the latch. The latch will only be updated at the next clock pulse. */
    public void write(long v) {
        tmpValue = v;
    }
}
