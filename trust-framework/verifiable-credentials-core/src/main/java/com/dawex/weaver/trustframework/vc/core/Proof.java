package com.dawex.weaver.trustframework.vc.core;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;

import java.time.ZonedDateTime;

/**
 * @see <a href=="https://w3id.org/security#proof">Proof Schema</a>
 */
public record Proof(@JsonLdProperty(value = "type", namespace = Namespace.SERVICE_DESCRIPTION) String type,
                    @JsonLdProperty(value = "created", namespace = Namespace.SERVICE_DESCRIPTION) ZonedDateTime created,
                    @JsonLdProperty(value = "proofPurpose", namespace = Namespace.SERVICE_DESCRIPTION) String proofPurpose,
                    @JsonLdProperty(value = "verificationMethod", namespace = Namespace.SERVICE_DESCRIPTION) String verificationMethod,
                    @JsonLdProperty(value = "jws", namespace = Namespace.SERVICE_DESCRIPTION) String jws) {

	public static ProofBuilder builder() {
		return new ProofBuilder();
	}

	public static class ProofBuilder {
		private String type;

		private ZonedDateTime created;

		private String proofPurpose;

		private String verificationMethod;

		private String jws;

		ProofBuilder() {
		}

		public ProofBuilder type(String type) {
			this.type = type;
			return this;
		}

		public ProofBuilder created(ZonedDateTime created) {
			this.created = created;
			return this;
		}

		public ProofBuilder proofPurpose(String proofPurpose) {
			this.proofPurpose = proofPurpose;
			return this;
		}

		public ProofBuilder verificationMethod(String verificationMethod) {
			this.verificationMethod = verificationMethod;
			return this;
		}

		public ProofBuilder jws(String jws) {
			this.jws = jws;
			return this;
		}

		public Proof build() {
			return new Proof(type, created, proofPurpose, verificationMethod, jws);
		}

		@Override
		public String toString() {
			return "ProofBuilder{" +
					"type='" + type + '\'' +
					", created=" + created +
					", proofPurpose='" + proofPurpose + '\'' +
					", verificationMethod='" + verificationMethod + '\'' +
					", jws='" + jws + '\'' +
					'}';
		}
	}
}
