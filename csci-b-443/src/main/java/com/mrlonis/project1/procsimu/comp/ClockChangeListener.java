package com.mrlonis.project1.procsimu.comp;

/**
 * Interface to be implemented by each latch or by each object that wants to have its method clockChanged called between
 * each cycle.
 */
public interface ClockChangeListener {

    /** Will be called at each clock pulse. */
    void clockChanged();
}
