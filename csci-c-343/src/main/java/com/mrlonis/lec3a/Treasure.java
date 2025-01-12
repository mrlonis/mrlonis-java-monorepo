package com.mrlonis.lec3a;

class Treasure extends Entry {
    private String treasure;

    public Treasure(String treasure) {
        this.treasure = treasure;
    }

    public int next() {
        throw new ArrrException();
    }

    public boolean hasNext() {
        return false;
    }

    public String dig() {
        return treasure;
    }
}
