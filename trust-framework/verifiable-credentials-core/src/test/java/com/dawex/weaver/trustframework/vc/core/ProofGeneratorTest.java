package com.dawex.weaver.trustframework.vc.core;

import com.dawex.weaver.trustframework.vc.core.jose.JsonWebSignatureUtils;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class ProofGeneratorTest {

	@Test
	void shouldGenerateProof() throws ParseException {
		final JWKSet jwkSet = JWKSet.parse(Constant.JWK_SET);
		final JWK jwk = jwkSet.getKeys().get(0);

		final Proof actual = ProofGenerator.generateProof(Constant.JSON_LD, "verificationMethod", jwk);

		assertThat(actual).isNotNull();
		assertThat(actual.type()).isEqualTo("JsonWebSignature2020");
		assertThat(actual.created()).isNotNull();
		assertThat(actual.proofPurpose()).isEqualTo("assertionMethod");
		assertThat(actual.verificationMethod()).isEqualTo("verificationMethod");
		assertThat(actual.jws()).matches(jws -> JsonWebSignatureUtils.isSignatureValid(jws, Constant.JSON_LD, jwk.toPublicJWK()));
	}
}