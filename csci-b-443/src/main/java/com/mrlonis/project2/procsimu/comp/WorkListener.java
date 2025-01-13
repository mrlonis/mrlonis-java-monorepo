package com.mrlonis.project2.procsimu.comp;

/**
 * Interface to be implemented by each stage that wants to be executed at each cycle.
 *
 * @author Modified by Vincent Oberle.
 */
public interface WorkListener {

    /** Does the real work of each stage. Will be called at each cycle. */
    void work();
}
