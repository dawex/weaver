package com.dawex.weaver.trustframework.vc.model.serialization;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.FormatProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultFormatProvider implements FormatProvider {

	private final Map<String, String> formats = new HashMap<>();

	@Override
	public Optional<String> getFormat(String formatName) {
		return Optional.ofNullable(formats.get(formatName));
	}

	/**
	 * Save the specified format.
	 *
	 * @param formatName is the name used in {@link JsonLdProperty#formatName}
	 * @param format     is the format, following the {@link java.util.Formatter} syntax, where the argument is replaced by the attribute value
	 */
	public void setFormat(String formatName, String format) {
		formats.put(formatName, format);
	}
}
