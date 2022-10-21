package com.dawex.weaver.trustframework.vc.core.jose;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.io.error.RdfWriterException;
import com.apicatalog.rdf.io.error.UnsupportedContentException;
import com.apicatalog.rdf.spi.RdfProvider;
import com.dawex.weaver.trustframework.vc.core.jose.exception.SignatureException;
import com.nimbusds.jose.HeaderParameterNames;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import io.setl.rdf.normalization.RdfNormalize;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Set;

/**
 * Utility class for generating and validating a signature for a JSON-LD document
 */
public class JsonWebSignatureUtils {

	private static final String HASH_ALGORITHM_SHA256 = "SHA-256";

	private JsonWebSignatureUtils() {
		// no instance allowed
	}

	/**
	 * Generate the signature of the serialized JSON-LD document, using the provided JSON Web Key
	 *
	 * @throws SignatureException If an error occurs during the signature generation
	 */
	public static String generateSignature(String jsonLd, JWK jwk) {
		try {
			final String normalized = normalize(jsonLd);
			final String hash = getHash(normalized);
			return getSignature(hash, jwk);

		} catch (JsonLdError | IOException | RdfWriterException | UnsupportedContentException | NoSuchAlgorithmException |
		         JOSEException e) {
			throw new SignatureException(e);
		}
	}

	/**
	 * Checks signature validity using the provided JSON Web Key
	 *
	 * @param signature the signature to validate
	 * @param jsonLd    the serialized JSON-LD document (a verifiable credential without the proof)
	 * @param jwk       JSON Web Key
	 * @throws SignatureException If an error occurs during the signature validation
	 */
	public static boolean isSignatureValid(String signature, String jsonLd, JWK jwk) {
		try {
			final RSASSAVerifier verifier = new RSASSAVerifier((RSAKey) jwk);
			return verifySignature(jsonLd, signature, verifier);

		} catch (JsonLdError | IOException | RdfWriterException | UnsupportedContentException | NoSuchAlgorithmException | ParseException |
		         JOSEException e) {
			throw new SignatureException(e);
		}
	}

	/**
	 * Checks signature validity using the provided X.509 certificate
	 *
	 * @param signature   the signature to validate
	 * @param jsonLd      the serialized JSON-LD document (a verifiable credential without the proof)
	 * @param certificate X.509 certificate
	 * @throws SignatureException If an error occurs during the signature validation
	 */
	public static boolean isSignatureValid(String signature, String jsonLd, X509Certificate certificate) {
		try {
			final RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) certificate.getPublicKey());
			return verifySignature(jsonLd, signature, verifier);

		} catch (JsonLdError | IOException | RdfWriterException | UnsupportedContentException | NoSuchAlgorithmException | ParseException |
		         JOSEException e) {
			throw new SignatureException(e);
		}
	}

	/**
	 * Normalizes JSON-LD input based on
	 * <a href="https://w3c.github.io/json-ld-api/#rdf-serialization-deserialization-algorithms">RDF Serialization/Deserialization Algorithms</a>
	 * <a href="https://w3c-ccg.github.io/rdf-dataset-canonicalization/spec/index.html">RDF Dataset Canonicalization</a>
	 */
	private static String normalize(String input) throws JsonLdError, IOException, RdfWriterException, UnsupportedContentException {
		// Load JsonDocument
		final JsonDocument jsonDocument;
		try (final Reader reader = new StringReader(input)) {
			jsonDocument = JsonDocument.of(reader);
		}
		// Convert to RDF
		final RdfDataset rdfDataset = JsonLd.toRdf(jsonDocument).get();
		// Normalize RDF with URDNA 2015 algorithm
		final RdfDataset normalized = RdfNormalize.normalize(rdfDataset);
		// Export RDF as String
		try (final StringWriter writer = new StringWriter()) {
			RdfProvider.provider().createWriter(MediaType.N_QUADS, writer).write(normalized);
			return writer.toString();
		}
	}

	/**
	 * Gets SHA-256 hash of input String
	 */
	private static String getHash(String input) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM_SHA256);
		final byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		return byteArrayToHex(hashBytes);
	}

	/**
	 * Converts a byte array to a hexadecimal String
	 */
	private static String byteArrayToHex(byte[] bytes) {
		final StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * Generates the signature of the input, according to <a href="https://w3c-ccg.github.io/lds-jws2020/#json-web-signature-2020">JSON Web Signature 2020 specifications</a>
	 */
	private static String getSignature(String input, JWK jwk) throws JOSEException {
		final JWSObject jwsObject = new JWSObject(
				new JWSHeader.Builder(JWSAlgorithm.PS256)
						.base64URLEncodePayload(false)
						.criticalParams(Set.of(HeaderParameterNames.BASE64_URL_ENCODE_PAYLOAD))
						.build(),
				new Payload(input));

		final RSASSASigner signer = new RSASSASigner((RSAKey) jwk);
		jwsObject.sign(signer);

		return jwsObject.serialize(true);
	}

	/**
	 * Checks the signature of the jsonLd with the specified verifier. The jsonLd is normalized before checking the signature.
	 */
	private static boolean verifySignature(String jsonLd, String signature, RSASSAVerifier verifier)
			throws JsonLdError, IOException, RdfWriterException, UnsupportedContentException, NoSuchAlgorithmException, ParseException,
			JOSEException {
		final String normalized = normalize(jsonLd);
		final String hash = getHash(normalized);

		final JWSObject jwsObject = JWSObject.parse(signature, new Payload(hash));
		return jwsObject.verify(verifier);
	}
}
