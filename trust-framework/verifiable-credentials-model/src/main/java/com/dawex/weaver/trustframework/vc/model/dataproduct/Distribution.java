package com.dawex.weaver.trustframework.vc.model.dataproduct;

import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;

import java.util.Objects;

import static com.dawex.weaver.trustframework.vc.core.Namespace.DAWEX;

public class Distribution {
	@JsonLdProperty(value = "title", namespace = DAWEX, mandatory = true)
	private final String title;

	@JsonLdProperty(value = "mediaType", namespace = DAWEX, mandatory = true)
	private final String mediaType;

	@JsonLdProperty(value = "byteSize", namespace = DAWEX, mandatory = true)
	private final Long byteSize;

	@JsonLdProperty(value = "fileHash", namespace = DAWEX, mandatory = true)
	private final String fileHash;

	@JsonLdProperty(value = "algorithm", namespace = DAWEX, mandatory = true)
	private final String algorithm;

	@JsonLdProperty(value = "location", namespace = DAWEX)
	private final Location location;

	public Distribution(String title, String mediaType, Long byteSize, String fileHash, String algorithm, Location location) {
		this.title = title;
		this.mediaType = mediaType;
		this.byteSize = byteSize;
		this.fileHash = fileHash;
		this.algorithm = algorithm;
		this.location = location;
	}

	public static DistributionBuilder builder() {
		return new DistributionBuilder();
	}

	public String getTitle() {
		return title;
	}

	public String getMediaType() {
		return mediaType;
	}

	public Long getByteSize() {
		return byteSize;
	}

	public String getFileHash() {
		return fileHash;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (Distribution) obj;
		return Objects.equals(this.title, that.title) &&
				Objects.equals(this.mediaType, that.mediaType) &&
				Objects.equals(this.byteSize, that.byteSize) &&
				Objects.equals(this.fileHash, that.fileHash) &&
				Objects.equals(this.algorithm, that.algorithm) &&
				Objects.equals(this.location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, mediaType, byteSize, fileHash, algorithm, location);
	}

	@Override
	public String toString() {
		return "Distribution[" +
				"title=" + title + ", " +
				"mediaType=" + mediaType + ", " +
				"byteSize=" + byteSize + ", " +
				"fileHash=" + fileHash + ", " +
				"algorithm=" + algorithm + ", " +
				"location=" + location + ']';
	}

	public static class DistributionBuilder {
		private String title;

		private String mediaType;

		private Long byteSize;

		private String fileHash;

		private String algorithm;

		private Location location;

		DistributionBuilder() {
		}

		public DistributionBuilder title(String title) {
			this.title = title;
			return this;
		}

		public DistributionBuilder mediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}

		public DistributionBuilder byteSize(Long byteSize) {
			this.byteSize = byteSize;
			return this;
		}

		public DistributionBuilder fileHash(String fileHash) {
			this.fileHash = fileHash;
			return this;
		}

		public DistributionBuilder algorithm(String algorithm) {
			this.algorithm = algorithm;
			return this;
		}

		public DistributionBuilder location(Location location) {
			this.location = location;
			return this;
		}

		public Distribution build() {
			return new Distribution(title, mediaType, byteSize, fileHash, algorithm, location);
		}

		@Override
		public String toString() {
			return "DistributionBuilder{" +
					"title='" + title + '\'' +
					", mediaType='" + mediaType + '\'' +
					", byteSize=" + byteSize +
					", fileHash='" + fileHash + '\'' +
					", algorithm='" + algorithm + '\'' +
					", location=" + location +
					'}';
		}
	}
}
