package com.dawex.weaver.trustframework.vc.core.jose.exception;

import java.io.Serial;

/**
 * This exception is thrown when an error occurs during the signature of a verifiable credential
 */
public class SignatureException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -4286377038818937242L;

	public SignatureException() {
	}

	public SignatureException(String message) {
		super(message);
	}

	public SignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignatureException(Throwable cause) {
		super(cause);
	}
}
