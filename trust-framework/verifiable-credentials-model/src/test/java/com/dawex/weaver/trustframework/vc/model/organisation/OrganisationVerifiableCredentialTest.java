package com.dawex.weaver.trustframework.vc.model.organisation;

import com.dawex.weaver.trustframework.vc.core.ProofGenerator;
import com.dawex.weaver.trustframework.vc.core.SignedObject;
import com.dawex.weaver.trustframework.vc.core.jose.JwkSetUtils;
import com.dawex.weaver.trustframework.vc.core.jsonld.serialization.FormatProvider;
import com.dawex.weaver.trustframework.vc.model.Address;
import com.dawex.weaver.trustframework.vc.model.serialization.DefaultFormatProvider;
import com.dawex.weaver.trustframework.vc.model.serialization.JacksonModuleFactory;
import com.dawex.weaver.trustframework.vc.model.utils.ProofSignatureExpectationsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jose.jwk.JWK;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

import static com.dawex.weaver.trustframework.vc.model.serialization.Format.ORGANISATION_CREDENTIAL_SUBJECT;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.ORGANISATION_ISSUER;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.ORGANISATION_VERIFIABLE_CREDENTIAL;
import static com.dawex.weaver.trustframework.vc.model.utils.TestUtils.assertThatJsonListValue;
import static com.dawex.weaver.trustframework.vc.model.utils.TestUtils.assertThatJsonStringValue;
import static org.assertj.core.api.Assertions.assertThat;

class OrganisationVerifiableCredentialTest {

	private static final ObjectMapper OBJECT_MAPPER = getObjectMapper();

	private static final JwkSetUtils.CreatedKeys CREATED_KEYS = JwkSetUtils.createKeys("Test", 12);

	private final static JWK JWK = CREATED_KEYS.jwkSet().getKeys().stream().findFirst().orElseThrow();

	@Test
	void shouldGenerateValidVerifiableCredentialForOrganisation() throws JsonProcessingException {
		// given
		final var verifiableCredential = getOrganisationVerifiableCredential();

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
		assertThatJsonStringValue("$['@context']['gax-participant']", serializedVc).isEqualTo("https://w3id.org/gaia-x/participant#");
		assertThatJsonListValue("$['@type']", serializedVc)
				.hasSize(2)
				.containsExactlyInAnyOrder("VerifiableCredential", "LegalPerson");
		assertThatJsonStringValue("$['@id']", serializedVc)
				.isEqualTo("./organisations/62b573deb33e417edcb34-id/verifiableCredential");
		assertThatJsonStringValue("$['sd:issuer']", serializedVc).isEqualTo("./organisations/62b573deb33e417ed-issuer");
		assertThatJsonStringValue("$['sd:issuanceDate']", serializedVc).isEqualTo("2022-07-28T15:16:01Z");
		assertThatJsonStringValue("$['sd:credentialSubject']['@id']", serializedVc)
				.isEqualTo("./organisations/62b573deb33e417e-company");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:name']", serializedVc)
				.isEqualTo("Mercat de la Boqueria");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:registrationNumber']", serializedVc)
				.isEqualTo("AB-1234-YZ");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:headquarterAddress']['gax-participant:street-address']",
				serializedVc)
				.isEqualTo("La Rambla, 91");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:headquarterAddress']['gax-participant:postal-code']",
				serializedVc)
				.isEqualTo("08001");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:headquarterAddress']['gax-participant:region']", serializedVc)
				.isEqualTo("Cataluña");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:headquarterAddress']['gax-participant:locality']",
				serializedVc)
				.isEqualTo("Barcelona");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:headquarterAddress']['gax-participant:country-name']",
				serializedVc)
				.isEqualTo("ESP");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:legalAddress']['gax-participant:street-address']",
				serializedVc)
				.isEqualTo("7 rue Grenette");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:legalAddress']['gax-participant:postal-code']", serializedVc)
				.isEqualTo("74000");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:legalAddress']['gax-participant:region']", serializedVc)
				.isEqualTo("Savoie");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:legalAddress']['gax-participant:locality']", serializedVc)
				.isEqualTo("Annecy");
		assertThatJsonStringValue("$['sd:credentialSubject']['gax-participant:legalAddress']['gax-participant:country-name']", serializedVc)
				.isEqualTo("FRA");
		assertThatJsonStringValue("$['sd:proof']['sd:type']", serializedVc).isEqualTo("JsonWebSignature2020");
		assertThat(JsonPath.compile("$['sd:proof']['sd:created']")).isNotNull();
		assertThatJsonStringValue("$['sd:proof']['sd:proofPurpose']", serializedVc).isEqualTo("assertionMethod");
		assertThatJsonStringValue("$['sd:proof']['sd:verificationMethod']", serializedVc)
				.isEqualTo("https://dawex.com/api/jwks");
		new ProofSignatureExpectationsHelper(CREATED_KEYS.jwkSet(), CREATED_KEYS.certificates()).assertSignatureIsValid(serializedVc);
	}

	private static OrganisationVerifiableCredential getOrganisationVerifiableCredential() {
		return OrganisationVerifiableCredential.builder()
				.id("62b573deb33e417edcb34-id")
				.issuer("62b573deb33e417ed-issuer")
				.issuanceDate(LocalDate.of(2022, Month.JULY, 28).atTime(15, 16, 1).atZone(ZoneOffset.UTC))
				.organisationCredentialSubject(OrganisationCredentialSubject.builder()
						.id("62b573deb33e417e-company")
						.name("Mercat de la Boqueria")
						.registrationNumber("AB-1234-YZ")
						.headquarterAddress(Address.builder()
								.streetAddress("La Rambla, 91")
								.postalCode("08001")
								.region("Cataluña")
								.locality("Barcelona")
								.countryName("ESP")
								.build())
						.legalAddress(Address.builder()
								.streetAddress("7 rue Grenette")
								.postalCode("74000")
								.region("Savoie")
								.locality("Annecy")
								.countryName("FRA")
								.build())
						.build())
				.build();
	}

	private static ObjectMapper getObjectMapper() {
		final var objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.registerModule(JacksonModuleFactory.organisationSerializationModule(getFormatProvider(), () -> "https://dawex.com"));
		return objectMapper;
	}

	private static FormatProvider getFormatProvider() {
		final DefaultFormatProvider formatProvider = new DefaultFormatProvider();
		formatProvider.setFormat(ORGANISATION_VERIFIABLE_CREDENTIAL, "./organisations/%s/verifiableCredential");
		formatProvider.setFormat(ORGANISATION_CREDENTIAL_SUBJECT, "./organisations/%s");
		formatProvider.setFormat(ORGANISATION_ISSUER, "./organisations/%s");
		return formatProvider;
	}
}
