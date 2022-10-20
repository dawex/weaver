package com.dawex.weaver.trustframework.vc.model.dataproduct;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;

import java.util.Collection;
import java.util.Objects;

import static com.dawex.weaver.trustframework.vc.core.Namespace.DAWEX;
import static com.dawex.weaver.trustframework.vc.core.Namespace.GAIAX_RESOURCE;
import static com.dawex.weaver.trustframework.vc.model.serialization.Format.DATA_PRODUCT_COPYRIGHT_OWNED_BY;

/**
 * @see <a href="http://w3id.org/gaia-x/service#aggregationOf">AggregationOf Schema</a>
 */
public class AggregationOf {
	@JsonLdProperty(value = "identifier", namespace = DAWEX, mandatory = true)
	private final String id;

	@JsonLdProperty(value = "copyrightOwnedBy", namespace = GAIAX_RESOURCE, formatName = DATA_PRODUCT_COPYRIGHT_OWNED_BY, mandatory = true)
	private final String copyrightOwnedBy;

	@JsonLdProperty(value = "license", namespace = GAIAX_RESOURCE, mandatory = true)
	private final Collection<String> licenses;

	@JsonLdProperty(value = "personalDataPolicy", namespace = DAWEX)
	private final PersonalDataPolicy personalDataPolicy;

	@JsonLdProperty(value = "distribution", namespace = DAWEX)
	private final Collection<Distribution> distributions;

	public AggregationOf(String id, String copyrightOwnedBy, Collection<String> licenses, PersonalDataPolicy personalDataPolicy,
			Collection<Distribution> distributions) {
		this.id = id;
		this.copyrightOwnedBy = copyrightOwnedBy;
		this.licenses = licenses;
		this.personalDataPolicy = personalDataPolicy;
		this.distributions = distributions;
	}

	public static AggregationOfBuilder builder() {
		return new AggregationOfBuilder();
	}

	public String getId() {
		return id;
	}

	public String getCopyrightOwnedBy() {
		return copyrightOwnedBy;
	}

	public Collection<String> getLicenses() {
		return licenses;
	}

	public PersonalDataPolicy getPersonalDataPolicy() {
		return personalDataPolicy;
	}

	public Collection<Distribution> getDistributions() {
		return distributions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (AggregationOf) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.copyrightOwnedBy, that.copyrightOwnedBy) &&
				Objects.equals(this.licenses, that.licenses) &&
				Objects.equals(this.personalDataPolicy, that.personalDataPolicy) &&
				Objects.equals(this.distributions, that.distributions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, copyrightOwnedBy, licenses, personalDataPolicy, distributions);
	}

	@Override
	public String toString() {
		return "AggregationOf[" +
				"id=" + id + ", " +
				"copyrightOwnedBy=" + copyrightOwnedBy + ", " +
				"licenses=" + licenses + ", " +
				"personalDataPolicy=" + personalDataPolicy + ", " +
				"distributions=" + distributions + ']';
	}

	public static class AggregationOfBuilder {
		private String id;

		private String copyrightOwnedBy;

		private Collection<String> licenses;

		private PersonalDataPolicy personalDataPolicy;

		private Collection<Distribution> distributions;

		AggregationOfBuilder() {
		}

		public AggregationOfBuilder id(String id) {
			this.id = id;
			return this;
		}

		public AggregationOfBuilder copyrightOwnedBy(String copyrightOwnedBy) {
			this.copyrightOwnedBy = copyrightOwnedBy;
			return this;
		}

		public AggregationOfBuilder licenses(Collection<String> licenses) {
			this.licenses = licenses;
			return this;
		}

		public AggregationOfBuilder personalDataPolicy(PersonalDataPolicy personalDataPolicy) {
			this.personalDataPolicy = personalDataPolicy;
			return this;
		}

		public AggregationOfBuilder distributions(Collection<Distribution> distributions) {
			this.distributions = distributions;
			return this;
		}

		public AggregationOf build() {
			return new AggregationOf(id, copyrightOwnedBy, licenses, personalDataPolicy, distributions);
		}

		@Override
		public String toString() {
			return "AggregationOfBuilder{" +
					"id='" + id + '\'' +
					", copyrightOwnedBy='" + copyrightOwnedBy + '\'' +
					", licenses=" + licenses +
					", personalDataPolicy=" + personalDataPolicy +
					", distributions=" + distributions +
					'}';
		}
	}
}
