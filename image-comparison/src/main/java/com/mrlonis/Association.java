package com.mrlonis;

/**
 * @author mrlonis
 */
public class Association {

    public int key;
    public long value;

    public Association(int key, long value) {
        this.key = key;
        this.value = value;
    }

    public static void main(String[] args) {
        Association a1 = new Association(1, 2);

        System.out.println("a1 key = " + a1.key + " value = " + a1.value);
    }

}
