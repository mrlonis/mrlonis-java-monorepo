package com.mrlonis.lec3a;

class Clue extends Entry {
    private int k;

    public Clue(int k) {
        this.k = k;
    }

    public int next() {
        return k;
    }

    public boolean hasNext() {
        return true;
    }

    public String dig() {
        throw new ArrrException();
    }
}
