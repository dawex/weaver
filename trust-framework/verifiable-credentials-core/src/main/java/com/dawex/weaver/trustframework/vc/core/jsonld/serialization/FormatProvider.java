package com.dawex.weaver.trustframework.vc.core.jsonld.serialization;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;

import java.util.Optional;

/**
 * Provides the formats used for JSON-LD property serialization
 */
public interface FormatProvider {

	/**
	 * Returns the format matching the specified format name
	 *
	 * @param formatName the format name, used in {@link JsonLdProperty#formatName}
	 */
	Optional<String> getFormat(String formatName);
}
