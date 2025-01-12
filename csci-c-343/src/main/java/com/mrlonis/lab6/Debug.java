package com.mrlonis.lab6;

public class Debug {

    public static int length(Node p) {
        return 1 + length(p.next);
    }

    public static void main(String[] args) {
        Node p = new Node(0, null);
        Node q = new Node(1, p);
        System.out.println(length(q));
    }
}

class Node {
    public Node(int data, Node node) {
        this.data = data;
        this.next = node;
    }

    int data;
    Node next;
}
