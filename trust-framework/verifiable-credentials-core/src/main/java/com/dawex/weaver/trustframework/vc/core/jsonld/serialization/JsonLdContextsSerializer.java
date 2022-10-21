package com.dawex.weaver.trustframework.vc.core.jsonld.serialization;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContext;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContexts;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.function.Supplier;

public class JsonLdContextsSerializer extends JsonSerializer<JsonLdContexts> {

	private static final String CONTEXT_BASE = "@base";

	private Supplier<String> baseIri;

	public JsonLdContextsSerializer(Supplier<String> baseIri) {
		this.baseIri = baseIri;
	}

	@Override
	public void serialize(JsonLdContexts jsonLdContexts, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeStartObject();
		if (jsonLdContexts.addBaseContext() && baseIri != null) {
			jsonGenerator.writeStringField(CONTEXT_BASE, baseIri.get());
		}
		for (JsonLdContext jsonLdContext : jsonLdContexts.value()) {
			jsonGenerator.writeStringField(jsonLdContext.term(), jsonLdContext.iri());
		}
		jsonGenerator.writeEndObject();
	}
}
