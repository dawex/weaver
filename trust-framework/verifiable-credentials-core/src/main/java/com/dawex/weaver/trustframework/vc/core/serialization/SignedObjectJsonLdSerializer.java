package com.dawex.weaver.trustframework.vc.core.serialization;

import com.dawex.weaver.trustframework.vc.core.SignedObject;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.FormatProvider;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.JsonLdSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SignedObjectJsonLdSerializer extends JsonLdSerializer<SignedObject> {

	public SignedObjectJsonLdSerializer(FormatProvider formatProvider) {
		super(SignedObject.class, formatProvider);
	}

	@Override
	public void serialize(SignedObject value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		final Class<?> serializablePayloadClass = value.payload().getClass();
		jsonGenerator.writeStartObject();
		writeContext(serializablePayloadClass, jsonGenerator);
		writeType(serializablePayloadClass, jsonGenerator);
		writeJsonLdProperties(value.payload(), serializablePayloadClass, jsonGenerator);
		writeJsonLdProperties(value, SignedObject.class, jsonGenerator);
		jsonGenerator.writeEndObject();
	}
}
