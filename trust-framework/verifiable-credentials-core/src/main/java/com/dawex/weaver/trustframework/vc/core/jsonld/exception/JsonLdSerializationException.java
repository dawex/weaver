package com.dawex.weaver.trustframework.vc.core.jsonld.exception;

import java.io.Serial;

/**
 * This exception is thrown when an error occurs during the JSON-LD serialization
 */
public class JsonLdSerializationException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -7594548319006577677L;

	public JsonLdSerializationException() {
	}

	public JsonLdSerializationException(String message) {
		super(message);
	}

	public JsonLdSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonLdSerializationException(Throwable cause) {
		super(cause);
	}
}
