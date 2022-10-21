package com.dawex.weaver.trustframework.vc.model.utils;

import com.dawex.weaver.trustframework.vc.core.jose.JsonWebSignatureUtils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.X509CertUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProofSignatureExpectationsHelper {

	private final JWKSet jwkSet;

	private final List<String> certificates;

	public ProofSignatureExpectationsHelper(JWKSet jwkSet, List<String> certificates) {
		this.jwkSet = jwkSet;
		this.certificates = certificates;
	}

	public void assertSignatureIsValid(String content) {
		final DocumentContext documentContext = JsonPath.parse(content);
		final String jws = documentContext.read("$['sd:proof']['sd:jws']", String.class);
		// get JSON-LD without the proof
		documentContext.delete("$['sd:proof']");
		final String jsonLd = documentContext.jsonString();

		assertThat(jwkSet.getKeys())
				.isNotEmpty().first()
				.matches(jwk -> JsonWebSignatureUtils.isSignatureValid(jws, jsonLd, jwk),
						"proof signature is valid (JWK)");

		assertThat(certificates)
				.isNotEmpty().first()
				.extracting(X509CertUtils::parse)
				.matches(certificate -> JsonWebSignatureUtils.isSignatureValid(jws, jsonLd, certificate),
						"proof signature is valid (X.509 certificate");
	}
}
