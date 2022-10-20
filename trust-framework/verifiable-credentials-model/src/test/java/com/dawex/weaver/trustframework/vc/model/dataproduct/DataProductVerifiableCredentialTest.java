package com.dawex.weaver.trustframework.vc.model.dataproduct;

import com.dawex.weaver.trustframework.vc.core.ProofGenerator;
import com.dawex.weaver.trustframework.vc.core.SignedObject;
import com.dawex.weaver.trustframework.vc.core.jose.JwkSetUtils;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.FormatProvider;
import com.dawex.weaver.trustframework.vc.model.serialization.DefaultFormatProvider;
import com.dawex.weaver.trustframework.vc.model.serialization.JacksonModuleFactory;
import com.dawex.weaver.trustframework.vc.model.utils.ProofSignatureExpectationsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nimbusds.jose.jwk.JWK;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_COPYRIGHT_OWNED_BY;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_CREDENTIAL_SUBJECT;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_ISSUER;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_PROVIDED_BY;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_VERIFIABLE_CREDENTIAL;
import static com.dawex.weaver.trustframework.vc.model.utils.TestUtils.assertThatJsonIntValue;
import static com.dawex.weaver.trustframework.vc.model.utils.TestUtils.assertThatJsonListValue;
import static com.dawex.weaver.trustframework.vc.model.utils.TestUtils.assertThatJsonStringValue;

class DataProductVerifiableCredentialTest {

	private static final ObjectMapper OBJECT_MAPPER = getObjectMapper();

	private static final JwkSetUtils.CreatedKeys CREATED_KEYS = JwkSetUtils.createKeys("Test", 12);

	private final static JWK JWK = CREATED_KEYS.jwkSet().getKeys().stream().findFirst().orElseThrow();

	@Test
	void shouldGenerateValidVerifiableCredentialForDataProduct() throws JsonProcessingException {
		// given
		final var verifiableCredential = getDataProductVerifiableCredential();

		// when
		final var proof = ProofGenerator.generateProof(
				OBJECT_MAPPER.writeValueAsString(verifiableCredential),
				"https://dawex.com/api/jwks",
				JWK);
		final var signedVc = new SignedObject<>(verifiableCredential, proof);
		final var serializedVc = OBJECT_MAPPER.writeValueAsString(signedVc);

		// then
		assertThatJsonStringValue("$['@context']['@base']", serializedVc).isEqualTo("https://dawex.com");
		assertThatJsonStringValue("$['@context']['sd']", serializedVc).isEqualTo("https://www.w3.org/2018/credentials/v1");
		assertThatJsonStringValue("$['@context']['gax-resource']", serializedVc).isEqualTo("https://www.w3id.org/gaia-x/resource#");
		assertThatJsonStringValue("$['@context']['gax-service']", serializedVc).isEqualTo("https://w3id.org/gaia-x/service#");
		assertThatJsonStringValue("$['@context']['dw']", serializedVc).isEqualTo("https://dawex.com/schemas/dataoffering#");
		assertThatJsonListValue("$['@type']", serializedVc)
				.hasSize(2)
				.containsExactlyInAnyOrder("VerifiableCredential", "ProductCredential");
		assertThatJsonStringValue("$['@id']", serializedVc)
				.isEqualTo("./organisations/62b570acb33e417edcb345ee/dataproducts/62bab5ae84fd784b1541e8f3/verifiableCredential");
		assertThatJsonStringValue("$['sd:issuer']", serializedVc).isEqualTo("./organisations/62b570acb33e417ed-issuer");
		assertThatJsonStringValue("$['sd:issuanceDate']", serializedVc).isEqualTo("2022-08-04T00:00:00Z");
		assertThatJsonStringValue("$['sd:credentialSubject']['@id']", serializedVc)
				.isEqualTo("./dataproducts/62bab5ae84fd784-dataproduct");
		assertThatJsonStringValue("$['sd:credentialSubject']['dw:title']", serializedVc)
				.isEqualTo("Statistics of road accidents in France");
		assertThatJsonStringValue("$['sd:credentialSubject']['dw:description']", serializedVc)
				.isEqualTo("This publication provides data on road accidents in France.");
		assertThatJsonStringValue("$['sd:credentialSubject']['dw:issued']", serializedVc).isEqualTo("2022-01-18T00:00:00Z");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-service:providedBy']", serializedVc)
				.isEqualTo("./organisations/62b570acb33e417-provider/verifiableCredential");
		assertThatJsonListValue("$['sd:credentialSubject']['gax-service:termsAndConditions']", serializedVc)
				.hasSize(3)
				.containsExactlyInAnyOrder("terms", "and", "conditions");
		assertThatJsonListValue("$['sd:credentialSubject']['gax-service:aggregationOf']", serializedVc).hasSize(1);
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:identifier']", serializedVc)
				.isEqualTo("62bac14584fd784b1541e9cb");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-service:aggregationOf'][0]['gax-resource:copyrightOwnedBy']",
				serializedVc)
				.isEqualTo("./organisations/62b570acb33e41-copyright/verifiableCredential");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:personalDataPolicy']", serializedVc)
				.isEqualTo("NO_PERSONAL_DATA");
		assertThatJsonListValue("$['sd:credentialSubject']['gax-service:aggregationOf'][0]['gax-resource:license']", serializedVc)
				.hasSize(2)
				.containsExactlyInAnyOrder("MIT", "LGPL-3.0");
		assertThatJsonListValue("$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution']", serializedVc)
				.hasSize(1);
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:title']", serializedVc)
				.isEqualTo("tangerine.csv");
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:mediaType']", serializedVc)
				.isEqualTo("text/csv");
		assertThatJsonIntValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:byteSize']", serializedVc)
				.isEqualTo(139855);
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:fileHash']", serializedVc)
				.isEqualTo("37f5d519788d497dcaaa345ba0cb9629fb13ffe23011eb7751796153985a86fb");
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:algorithm']", serializedVc)
				.isEqualTo("SHA-256");
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:location']['@type']", serializedVc)
				.isEqualTo("gax-service:TrustedCloudServiceOfferingSubCompanies");
		assertThatJsonStringValue(
				"$['sd:credentialSubject']['gax-service:aggregationOf'][0]['dw:distribution'][0]['dw:location']['gax-service:dataCenterLocation']",
				serializedVc)
				.isEqualTo("Europe (Ireland)");
		assertThatJsonStringValue("$['sd:proof']['sd:type']", serializedVc).isEqualTo("JsonWebSignature2020");
		assertThatJsonStringValue("$['sd:proof']['sd:created']", serializedVc).isNotNull();
		assertThatJsonStringValue("$['sd:proof']['sd:proofPurpose']", serializedVc).isEqualTo("assertionMethod");
		assertThatJsonStringValue("$['sd:proof']['sd:verificationMethod']", serializedVc).isEqualTo("https://dawex.com/api/jwks");
		new ProofSignatureExpectationsHelper(CREATED_KEYS.jwkSet(), CREATED_KEYS.certificates()).assertSignatureIsValid(serializedVc);
	}

	private static DataProductVerifiableCredential getDataProductVerifiableCredential() {
		return DataProductVerifiableCredential.builder()
				.id(new DataProductVerifiableCredential.Id("62bab5ae84fd784b1541e8f3", "62b570acb33e417edcb345ee"))
				.issuer("62b570acb33e417ed-issuer")
				.issuanceDate(LocalDate.of(2022, Month.AUGUST, 4).atStartOfDay(ZoneOffset.UTC))
				.credentialSubject(DataProductCredentialSubject.builder()
						.id("62bab5ae84fd784-dataproduct")
						.title("Statistics of road accidents in France")
						.description("This publication provides data on road accidents in France.")
						.issued(LocalDate.of(2022, Month.JANUARY, 18).atStartOfDay(ZoneOffset.UTC))
						.providedBy("62b570acb33e417-provider")
						.termsAndConditions(List.of("terms", "and", "conditions"))
						.aggregationOf(List.of(
								AggregationOf.builder()
										.id("62bac14584fd784b1541e9cb")
										.copyrightOwnedBy("62b570acb33e41-copyright")
										.licenses(List.of("MIT", "LGPL-3.0"))
										.personalDataPolicy(PersonalDataPolicy.NO_PERSONAL_DATA)
										.distributions(List.of(
												Distribution.builder()
														.title("tangerine.csv")
														.mediaType("text/csv")
														.byteSize(139855L)
														.fileHash("37f5d519788d497dcaaa345ba0cb9629fb13ffe23011eb7751796153985a86fb")
														.algorithm("SHA-256")
														.location(Location.builder()
																.dataCenterLocation("Europe (Ireland)")
																.build())
														.build()
										))
										.build()))
						.build())
				.build();
	}

	private static ObjectMapper getObjectMapper() {
		final var objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.registerModule(JacksonModuleFactory.dataProductSerializationModule(getFormatProvider(), () -> "https://dawex.com"));
		return objectMapper;
	}

	private static FormatProvider getFormatProvider() {
		final DefaultFormatProvider formatProvider = new DefaultFormatProvider();
		formatProvider.setFormat(DATA_PRODUCT_VERIFIABLE_CREDENTIAL, "./organisations/%s/dataproducts/%s/verifiableCredential");
		formatProvider.setFormat(DATA_PRODUCT_ISSUER, "./organisations/%s");
		formatProvider.setFormat(DATA_PRODUCT_CREDENTIAL_SUBJECT, "./dataproducts/%s");
		formatProvider.setFormat(DATA_PRODUCT_PROVIDED_BY, "./organisations/%s/verifiableCredential");
		formatProvider.setFormat(DATA_PRODUCT_COPYRIGHT_OWNED_BY, "./organisations/%s/verifiableCredential");
		return formatProvider;
	}
}
