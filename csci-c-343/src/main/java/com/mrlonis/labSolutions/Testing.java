package com.mrlonis.labSolutions;

public class Testing {

    public static void main(String[] args) {
        String yo = "00";
        System.out.println(yo);
        yo = yo.substring(1, yo.length());
        System.out.println(yo);
        /*
        ArrayList<Integer> lst = new ArrayList<>();
        LinkedList<Integer> lstL = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
        	System.out.println("i = " + i + "|| i % 10 = " + i%10);
        	double start = System.nanoTime();
        	lst.add(i);
        	double end = System.nanoTime();
        	System.out.println("Add Time ArrayList: " + (end - start));
        }
        System.out.println(lst);
        System.out.println("\n");
        for (int i = 0; i < 10; i++) {
        	System.out.println("i = " + i + "|| i % 10 = " + i%10);
        	double start = System.nanoTime();
        	lstL.add(i);
        	double end = System.nanoTime();
        	System.out.println("Add Time LinkedList: " + (end - start));
        }
        System.out.println(lst);
        */

        /*
        		 * Output:
        		 * i = 0|| i % 10 = 0
        Add Time ArrayList: 35408.0
        i = 1|| i % 10 = 1
        Add Time ArrayList: 1764.0
        i = 2|| i % 10 = 2
        Add Time ArrayList: 979.0
        i = 3|| i % 10 = 3
        Add Time ArrayList: 862.0
        i = 4|| i % 10 = 4
        Add Time ArrayList: 956.0
        i = 5|| i % 10 = 5
        Add Time ArrayList: 831.0
        i = 6|| i % 10 = 6
        Add Time ArrayList: 847.0
        i = 7|| i % 10 = 7
        Add Time ArrayList: 895.0
        i = 8|| i % 10 = 8
        Add Time ArrayList: 880.0
        i = 9|| i % 10 = 9
        Add Time ArrayList: 887.0
        [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]


        i = 0|| i % 10 = 0
        Add Time LinkedList: 5956.0
        i = 1|| i % 10 = 1
        Add Time LinkedList: 1564.0
        i = 2|| i % 10 = 2
        Add Time LinkedList: 1083.0
        i = 3|| i % 10 = 3
        Add Time LinkedList: 1166.0
        i = 4|| i % 10 = 4
        Add Time LinkedList: 1590.0
        i = 5|| i % 10 = 5
        Add Time LinkedList: 1339.0
        i = 6|| i % 10 = 6
        Add Time LinkedList: 1179.0
        i = 7|| i % 10 = 7
        Add Time LinkedList: 1074.0
        i = 8|| i % 10 = 8
        Add Time LinkedList: 1218.0
        i = 9|| i % 10 = 9
        Add Time LinkedList: 1122.0
        [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

        		 */
    }
}
