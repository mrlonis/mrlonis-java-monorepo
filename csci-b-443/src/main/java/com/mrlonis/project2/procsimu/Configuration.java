package com.mrlonis.project2.procsimu;

import com.mrlonis.project2.procsimu.comp.Const;
import java.nio.file.FileSystems;

/**
 * Configuration values. The constants are not declared final, so in the case of a modification of them, only this class
 * needs to be recompiled.
 */
public class Configuration {

    public static String VERSION = "Pipelined";

    /** The directory to show in the load window. */
    public static String DEFAULT_LOAD_DIRECTORY =
            ".." + FileSystems.getDefault().getSeparator() + "asm";

    /**
     * Indicates which messages should be printed in the output windows. It is a binary-coded integer: each bit
     * corresponds to a type of message to print.
     */
    public static int OUTPUT_LEVEL = Const.OUTPUT_ALWAYS;

    // **************
    // *** MEMORY ***
    // **************

    /** Size of the memory */
    public static int MEMORY_SIZE = 10_000;
}
