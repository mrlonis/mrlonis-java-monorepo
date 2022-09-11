package com.mrlonis;

/**
 * Exception to signal a problem with encoding.
 */
class EncodeException extends RuntimeException {
	/**
	 * Auto generated serial ID to avoid Eclipse Warning.
	 */
	private static final long serialVersionUID = 2960907589815740361L;

	EncodeException(Character ch) {
		super("Cannot encode " + ch);
	}
}
