package com.dawex.weaver.trustframework.vc.core;

import com.dawex.weaver.trustframework.vc.core.jose.JsonWebSignatureUtils;
import com.nimbusds.jose.jwk.JWK;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ProofGenerator {

	/**
	 * Generate the proof of the serialized JSON-LD document, using the provided JSON Web Key
	 */
	public static Proof generateProof(String jsonLd, String verificationMethod, JWK jwk) {
		final String signature = JsonWebSignatureUtils.generateSignature(jsonLd, jwk);
		return toProof(signature, verificationMethod);
	}

	private static Proof toProof(String signature, String verificationMethod) {
		return Proof.builder()
				.type("JsonWebSignature2020")
				.created(ZonedDateTime.now(ZoneOffset.UTC))
				.proofPurpose("assertionMethod")
				.verificationMethod(verificationMethod)
				.jws(signature)
				.build();
	}

	private ProofGenerator() {
		// no instance allowed
	}
}
