package com.mrlonis;

import java.util.HashMap;

/**
 * @author Matthew Lonis (mrlonis)
 */

public class FrequencyTable extends HashMap<Character, Integer> {
	/**
	 * Auto-Generated Serial ID to avoid Eclipse warning.
	 */
	private static final long serialVersionUID = 8741386648186269125L;

	/**
	 * Constructs an empty table.
	 */
	public FrequencyTable() {
		super();
	}

	/**
	 * Constructs a table of character counts from the given text string.
	 * 
	 * @param text
	 *            The text string used to construct the table of character
	 *            counts.
	 */
	public FrequencyTable(String text) {
		char[] a = text.toCharArray();

		for (char x : a) {
			/**
			 * Get current count and increment (if it exists).
			 */
			Integer count = this.get(x);
			count++;

			/**
			 * Put new character count into the table.
			 */
			this.put(x, count);
		}
	}

	/**
	 * Returns the count associated with the given character. In the case that
	 * there is no association of ch in the map, return 0.
	 * 
	 * @param ch
	 *            The character for which the count will be returned from the
	 *            Frequency Table.
	 * @return The count for the given character in the table.
	 */
	@Override
	public Integer get(Object ch) {
		/**
		 * Gets the character count from the table or defaults to zero.
		 */
		Integer answer = this.getOrDefault(ch, 0);
		return answer;
	}
}
