package com.mrlonis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * There's nothing for you to do here.
 */

public class Util {
	/**
	 * Reads a text file and returns the contents as a string.
	 */
	public static String loadFile(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner in = new Scanner(new File(filename));
			while (in.hasNextLine())
				sb.append(in.nextLine()).append("\n");
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("The input file: [" + filename + "] is not found.\n.");
		}
		return sb.toString();
	}

	/**
	 * Returns a bit string of length n, corresponding to the ASCII encoding of
	 * ch.
	 */
	public static String asciiToBits(char ch, int n) {
		return padLeft(Integer.toBinaryString((int) ch), n);
	}

	/**
	 * Returns the char whose ASCII value corresponds to the given bit string.
	 */
	public static char bitsToAscii(String bits) {
		return (char) Integer.parseInt(bits, 2);
	}

	/**
	 * Returns the result of padding s on the left with zeroes so that its total
	 * length is at least n. We use a StringBuilder for efficiency.
	 */
	public static String padLeft(String s, int n) {
		while (s.length() < n)
			s = '0' + s;
		return s;
	}
}
