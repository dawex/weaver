package com.dawex.weaver.trustframework.vc.core.jose.exception;

import java.io.Serial;

/**
 * This exception is thrown when an error occurs during the creation of public / private keys or certificate
 */
public class KeyParsingException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -4369178955612034214L;

	public KeyParsingException() {
	}

	public KeyParsingException(String message) {
		super(message);
	}

	public KeyParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyParsingException(Throwable cause) {
		super(cause);
	}
}
