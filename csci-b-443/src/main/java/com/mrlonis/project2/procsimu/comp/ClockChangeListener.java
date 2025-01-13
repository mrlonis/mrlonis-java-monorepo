package com.mrlonis.project2.procsimu.comp;

/**
 * Interface to be implemented by each latch or by each object that wants to have its method clockChanged called between
 * each cycle.
 *
 * @author Modified by Vincent Oberle.
 */
public interface ClockChangeListener {

    /** Will be called at each clock pulse. */
    void clockChanged();
}
