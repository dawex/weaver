package com.dawex.weaver.trustframework.vc.core.jose;

import com.dawex.weaver.trustframework.vc.core.Constant;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.X509CertUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class JsonWebSignatureUtilsTest {

	private static JWK jwk;

	private static X509Certificate certificate;

	@BeforeAll
	public static void init() throws ParseException {
		final JWKSet jwkSet = JWKSet.parse(Constant.JWK_SET);
		jwk = jwkSet.getKeys().get(0);
		certificate = X509CertUtils.parse(Constant.CERTIFICATE);
	}

	@Test
	void shouldGenerateAndValidateSignatureUsing() {
		assertThat(JsonWebSignatureUtils.generateSignature(Constant.JSON_LD, jwk))
				.isNotNull()
				.satisfies(signature -> {
					assertThat(JsonWebSignatureUtils.isSignatureValid(signature, Constant.JSON_LD, jwk.toPublicJWK())).isTrue();
					assertThat(JsonWebSignatureUtils.isSignatureValid(signature, Constant.JSON_LD, certificate)).isTrue();
				});
	}

	@Test
	void withInvalidSignatureShouldReturnFalse() throws ParseException {
		final String invalidSignature = "eyJhbGciOiJQUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..invalid-signature";

		assertThat(JsonWebSignatureUtils.isSignatureValid(invalidSignature, Constant.JSON_LD, jwk.toPublicJWK())).isFalse();
		assertThat(JsonWebSignatureUtils.isSignatureValid(invalidSignature, Constant.JSON_LD, certificate)).isFalse();
	}
}