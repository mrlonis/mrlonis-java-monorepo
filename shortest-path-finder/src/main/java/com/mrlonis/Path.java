package com.mrlonis;

import java.util.LinkedList;

/**
 * A Path represents a sequence of connected coordinates starting with wire.from and (presumably) ending with wire.to.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Path extends LinkedList<Coord> {

    /** Generated serial ID to suppress Eclipse warning. */
    private static final long serialVersionUID = 2827790888028951407L;

    protected Wire wire;

    public Path(Wire wire) {
        this.wire = wire;
        add(wire.from);
    }

    /** Returns the length of this path. */
    public int length() {
        return size();
    }
}
