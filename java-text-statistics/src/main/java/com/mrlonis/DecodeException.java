package com.mrlonis;

/**
 * Exception to signal a problem with decoding.
 */
public class DecodeException extends RuntimeException {
	/**
	 * Auto generated serial ID to avoid Eclipse Warning.
	 */
	private static final long serialVersionUID = -615245918173449135L;

	DecodeException(String bits) {
		super("Cannot decode " + bits);
	}
}