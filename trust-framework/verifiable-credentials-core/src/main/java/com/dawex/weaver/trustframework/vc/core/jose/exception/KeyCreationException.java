package com.dawex.weaver.trustframework.vc.core.jose.exception;

import java.io.Serial;

/**
 * This exception is thrown when an error occurs during the creation of public / private keys or certificate
 */
public class KeyCreationException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 6008976356986483703L;

	public KeyCreationException() {
	}

	public KeyCreationException(String message) {
		super(message);
	}

	public KeyCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyCreationException(Throwable cause) {
		super(cause);
	}
}
