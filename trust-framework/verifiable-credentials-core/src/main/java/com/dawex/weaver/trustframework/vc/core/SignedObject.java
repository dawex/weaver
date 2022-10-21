package com.dawex.weaver.trustframework.vc.core;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;

/**
 * @param payload SignedObject will be serialized as JSON-LD, so payload is expected to be JSON-LD serializable.
 * @see <a href="https://www.w3.org/TR/vc-data-model-2.0/">Verifiable Credentials Data Model v2.0</a>
 */
public record SignedObject<T>(
		T payload,
		@JsonLdProperty(value = "proof", namespace = Namespace.SERVICE_DESCRIPTION) Proof proof) {

	public static <T> SignedObjectBuilder<T> builder() {
		return new SignedObjectBuilder<>();
	}

	public static class SignedObjectBuilder<T> {
		private T payload;

		private Proof proof;

		SignedObjectBuilder() {
		}

		public SignedObjectBuilder<T> payload(T payload) {
			this.payload = payload;
			return this;
		}

		public SignedObjectBuilder<T> proof(Proof proof) {
			this.proof = proof;
			return this;
		}

		public SignedObject<T> build() {
			return new SignedObject<>(payload, proof);
		}

		@Override
		public String toString() {
			return "SignedObjectBuilder{" +
					"payload=" + payload +
					", proof=" + proof +
					'}';
		}
	}
}
