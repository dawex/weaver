package com.dawex.weaver.trustframework.vc.model.organisation;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContext;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContexts;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdType;

import java.time.ZonedDateTime;
import java.util.Objects;

import static com.dawex.weaver.trustframework.vc.core.Namespace.GAIAX_PARTICIPANT;
import static com.dawex.weaver.trustframework.vc.core.Namespace.GAIAX_PARTICIPANT_IRI;
import static com.dawex.weaver.trustframework.vc.core.Namespace.SERVICE_DESCRIPTION;
import static com.dawex.weaver.trustframework.vc.core.Namespace.SERVICE_DESCRIPTION_IRI;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.ORGANISATION_ISSUER;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.ORGANISATION_VERIFIABLE_CREDENTIAL;

/**
 * @see <a href="https://www.w3.org/2018/credentials/v1">Verifiable Credential Schema</a>
 */
@JsonLdContexts(addBaseContext = true, value = {
		@JsonLdContext(term = SERVICE_DESCRIPTION, iri = SERVICE_DESCRIPTION_IRI),
		@JsonLdContext(term = GAIAX_PARTICIPANT, iri = GAIAX_PARTICIPANT_IRI),
})
@JsonLdType({
		"VerifiableCredential",
		"LegalPerson"
})
public class OrganisationVerifiableCredential {
	@JsonLdProperty(value = "@id", formatName = ORGANISATION_VERIFIABLE_CREDENTIAL)
	private final String id;

	@JsonLdProperty(value = "issuer", namespace = SERVICE_DESCRIPTION, formatName = ORGANISATION_ISSUER)
	private final String issuer;

	@JsonLdProperty(value = "issuanceDate", namespace = SERVICE_DESCRIPTION)
	private final ZonedDateTime issuanceDate;

	@JsonLdProperty(value = "credentialSubject", namespace = SERVICE_DESCRIPTION)
	private final OrganisationCredentialSubject organisationCredentialSubject;

	public OrganisationVerifiableCredential(String id, String issuer, ZonedDateTime issuanceDate,
			OrganisationCredentialSubject organisationCredentialSubject) {
		this.id = id;
		this.issuer = issuer;
		this.issuanceDate = issuanceDate;
		this.organisationCredentialSubject = organisationCredentialSubject;
	}

	public static OrganisationVerifiableCredentialBuilder builder() {
		return new OrganisationVerifiableCredentialBuilder();
	}

	public String getId() {
		return id;
	}

	public String getIssuer() {
		return issuer;
	}

	public ZonedDateTime getIssuanceDate() {
		return issuanceDate;
	}

	public OrganisationCredentialSubject getOrganisationCredentialSubject() {
		return organisationCredentialSubject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (OrganisationVerifiableCredential) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.issuer, that.issuer) &&
				Objects.equals(this.issuanceDate, that.issuanceDate) &&
				Objects.equals(this.organisationCredentialSubject, that.organisationCredentialSubject);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, issuer, issuanceDate, organisationCredentialSubject);
	}

	@Override
	public String toString() {
		return "OrganisationVerifiableCredential[" +
				"id=" + id + ", " +
				"issuer=" + issuer + ", " +
				"issuanceDate=" + issuanceDate + ", " +
				"organisationCredentialSubject=" + organisationCredentialSubject + ']';
	}

	public static class OrganisationVerifiableCredentialBuilder {
		private String id;

		private String issuer;

		private ZonedDateTime issuanceDate;

		private OrganisationCredentialSubject organisationCredentialSubject;

		OrganisationVerifiableCredentialBuilder() {
		}

		public OrganisationVerifiableCredentialBuilder id(String id) {
			this.id = id;
			return this;
		}

		public OrganisationVerifiableCredentialBuilder issuer(String issuer) {
			this.issuer = issuer;
			return this;
		}

		public OrganisationVerifiableCredentialBuilder issuanceDate(ZonedDateTime issuanceDate) {
			this.issuanceDate = issuanceDate;
			return this;
		}

		public OrganisationVerifiableCredentialBuilder organisationCredentialSubject(
				OrganisationCredentialSubject organisationCredentialSubject) {
			this.organisationCredentialSubject = organisationCredentialSubject;
			return this;
		}

		public OrganisationVerifiableCredential build() {
			return new OrganisationVerifiableCredential(id, issuer, issuanceDate, organisationCredentialSubject);
		}

		@Override
		public String toString() {
			return "OrganisationVerifiableCredentialBuilder{" +
					"id='" + id + '\'' +
					", issuer='" + issuer + '\'' +
					", issuanceDate=" + issuanceDate +
					", organisationCredentialSubject=" + organisationCredentialSubject +
					'}';
		}
	}
}
