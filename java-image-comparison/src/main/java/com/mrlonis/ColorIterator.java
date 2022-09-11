package com.mrlonis;

import java.awt.Color;

/**
 * This class represents an iterator that returns all the colors in a color key space
 * based on a certain bits per channel. Bits per channel can be anywhere from 1 to 8.
 * 
 * @author mrlonis
 */

public class ColorIterator implements Iterator {
	/**
	 * Represents the increment amount for the RGB values in the color space.
	 */
	private int inc;
	
	/**
	 * Represents the ColorTable that this ColorIterator will use ColorTable.get() on.
	 */
	private ColorTable colorTable;
	
	/**
	 * Represents the Red value in the RGB spectrum.
	 */
	private int r;
	
	/**
	 * Represents the Green value in the RGB spectrum.
	 */
	private int g;
	
	/**
	 * Represents the Blue value in the RGB spectrum.
	 */
	private int b;
	
	/**
	 * Represents whether or not this is this first time hasNext() has process. Is <code>true</code>
	 * if it is the first time hasNext() is called or if the r, g, and b values are all 0.
	 * Is <code>false</code> otherwise.
	 */
	private boolean initial;
	
	/**
	 * Constructs a ColorIterator with a ColorTable parameter that will be stored in the Iterator 
	 * instance variable colorTable.
	 * 
	 * @param	colorTable	The ColorTable that has called for an Iterator.
	 */
	public ColorIterator(ColorTable colorTable) {
		this.initial = true;
		this.colorTable = colorTable;
		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.inc = (int) Math.pow(2, (8 - colorTable.bitsPerChannel));
	}
	
	/**
	 * Returns the frequency count of the color specified by the r, g, and b values in colorTable.
	 * Increments the r, g, and b values so the next time next() is called the r, g, and b
	 * values are incremented by inc.
	 * 
	 * @return	The frequency count associated with the color in colorTable specified by 
	 * 			the r, g, and b variables
	 */
	public long next() {
		Color color = new Color(r, g, b);
		long value = colorTable.get(color);
		
		b += inc;
		this.initial = false;
		
		if (b > 255) {
			b = 0;
			g += inc;
			if (g > 255) {
				g = 0;
				r += inc;
				if (r > 255) {
					r = 0;
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Returns <code>true</code> if initial is true or if r, g, and b aren't all equal to 0. 
	 * <code>false</code> otherwise.
	 * 
	 * @return Returns <code>true</code> if the ColorIterator has another color in the color space.
	 * 		   Otherwise, returns <code>flase</code>.
	 */
	public boolean hasNext() {
		if (initial == true) {
			return true;
		} else {
			if ((r == 0) && (g == 0) && (b == 0)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Simple Testing
	 */
	public static void main(String[] args) {
		System.out.println("ColorIterator Testing...");
		ColorTable table = new ColorTable(3, 6, Constants.QUADRATIC, .49);
 		int[] data = new int[] { 32960, 4293315, 99011, 296390 };
 		for (int i = 0; i < data.length; i++) {
 			table.increment(new Color(data[i]));
 		}
 		
 		Iterator it = table.iterator();
 		
 		while (it.hasNext()) {
 			System.out.println(it.next());
 		}
 		System.out.println("... End");
	}
}
