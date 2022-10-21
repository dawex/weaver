package com.dawex.weaver.trustframework.vc.model.serialization;

import com.dawex.weaver.trustframework.vc.core.Proof;
import com.dawex.weaver.trustframework.vc.core.SignedObject;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContexts;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.FormatProvider;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.JsonLdContextsSerializer;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.JsonLdSerializer;
import com.dawex.weaver.trustframework.vc.core.serialization.SignedObjectJsonLdSerializer;
import com.dawex.weaver.trustframework.vc.model.Address;
import com.dawex.weaver.trustframework.vc.model.dataproduct.AggregationOf;
import com.dawex.weaver.trustframework.vc.model.dataproduct.DataProductCredentialSubject;
import com.dawex.weaver.trustframework.vc.model.dataproduct.DataProductVerifiableCredential;
import com.dawex.weaver.trustframework.vc.model.dataproduct.Distribution;
import com.dawex.weaver.trustframework.vc.model.dataproduct.Location;
import com.dawex.weaver.trustframework.vc.model.organisation.OrganisationCredentialSubject;
import com.dawex.weaver.trustframework.vc.model.organisation.OrganisationVerifiableCredential;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.function.Supplier;

public class JacksonModuleFactory {

	/**
	 * Create a configured Jackson module for serializing organisation verifiable credentials
	 */
	public static Module organisationSerializationModule(FormatProvider formatProvider, Supplier<String> baseIriSupplier) {
		final SimpleModule module = new SimpleModule();
		module.addSerializer(Address.class, new JsonLdSerializer<>(Address.class, formatProvider));
		module.addSerializer(OrganisationCredentialSubject.class, new JsonLdSerializer<>(OrganisationCredentialSubject.class,
				formatProvider));
		module.addSerializer(JsonLdContexts.class, new JsonLdContextsSerializer(baseIriSupplier));
		module.addSerializer(OrganisationVerifiableCredential.class, new JsonLdSerializer<>(OrganisationVerifiableCredential.class,
				formatProvider));
		module.addSerializer(Proof.class, new JsonLdSerializer<>(Proof.class, formatProvider));
		module.addSerializer(SignedObject.class, new SignedObjectJsonLdSerializer(formatProvider));
		return module;
	}

	/**
	 * Create a configured Jackson module for serializing data product verifiable credentials
	 */
	public static Module dataProductSerializationModule(FormatProvider formatProvider, Supplier<String> baseIriSupplier) {
		final SimpleModule module = new SimpleModule();
		module.addSerializer(AggregationOf.class, new JsonLdSerializer<>(AggregationOf.class, formatProvider));
		module.addSerializer(DataProductCredentialSubject.class,
				new JsonLdSerializer<>(DataProductCredentialSubject.class, formatProvider));
		module.addSerializer(Distribution.class, new JsonLdSerializer<>(Distribution.class, formatProvider));
		module.addSerializer(JsonLdContexts.class, new JsonLdContextsSerializer(baseIriSupplier));
		module.addSerializer(Location.class, new JsonLdSerializer<>(Location.class, formatProvider));
		module.addSerializer(DataProductVerifiableCredential.class,
				new JsonLdSerializer<>(DataProductVerifiableCredential.class, formatProvider));
		module.addSerializer(Proof.class, new JsonLdSerializer<>(Proof.class, formatProvider));
		module.addSerializer(SignedObject.class, new SignedObjectJsonLdSerializer(formatProvider));
		return module;
	}

	private JacksonModuleFactory() {
		// no instance allowed
	}
}
