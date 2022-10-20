package com.dawex.weaver.trustframework.vc.core.jose;

import com.dawex.weaver.trustframework.vc.core.jose.exception.KeyCreationException;
import com.dawex.weaver.trustframework.vc.core.jose.exception.KeyParsingException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.X509CertUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Utility class for handling JwkSets
 */
public class JwkSetUtils {

	/**
	 * JWT are signed with RS256 algorithm, that is an RSA signature with SHA-256.
	 * It's a popular algorithm, and the default one in NimbusReactiveJwtDecoder (used by the resources servers).
	 * The minimum recommended size for RSA key is 2048 bits.
	 */
	private static final int RSA_KEY_SIZE = 2048;

	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	private JwkSetUtils() {
		// no instance allowed
	}

	/**
	 * Parses the specified JSON object representing a JSON Web Key (JWK) set.
	 *
	 * @throws KeyParsingException If the data map couldn't be parsed to a valid JSON Web Key (JWK) set.
	 */
	public static JWKSet parseJwkSet(Map<String, Object> data) {
		try {
			return JWKSet.parse(data);
		} catch (ParseException e) {
			throw new KeyParsingException(e);
		}
	}

	/**
	 * Creates a new Jwk Set, and the associated X.509 certificate
	 *
	 * @throws KeyCreationException If the keys cannot be created
	 */
	public static CreatedKeys createKeys(String certificateIssuerCommonName, int certificateValidityInMonths) {
		try {
			final KeyPair keyPair = generateRsaKey();
			final X509Certificate cert = getSelfSignedX509Certificate(keyPair, certificateIssuerCommonName, certificateValidityInMonths);
			final RSAKey rsaKey = new RSAKey.Builder(RSAKey.parse(cert))
					.privateKey((RSAPrivateKey) keyPair.getPrivate())
					.build();
			return new CreatedKeys(new JWKSet(rsaKey), List.of(X509CertUtils.toPEMString(cert)));
		} catch (OperatorCreationException | CertificateException | JOSEException e) {
			throw new KeyCreationException("The key pair and/or the X.509 certificate cannot be created", e);
		}
	}

	private static KeyPair generateRsaKey() {
		final KeyPair keyPair;
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(RSA_KEY_SIZE);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (InvalidParameterException | NoSuchAlgorithmException e) {
			throw new KeyCreationException("The RSA key pair cannot be generated", e);
		}
		return keyPair;
	}

	/**
	 * Generate a self-signed certificate from the specified keyPair
	 */
	private static X509Certificate getSelfSignedX509Certificate(KeyPair keyPair, String commonName, int validityInMonths)
			throws OperatorCreationException, CertificateException {
		// arbitrary X500Name
		final X500Name name = new X500Name("CN=%s".formatted(commonName));
		// certificate serial number https://www.rfc-editor.org/rfc/rfc3280#section-4.1.2.2
		final BigInteger serial = BigInteger.valueOf(new SecureRandom().nextLong(0, Long.MAX_VALUE));
		// certificate validity arbitrary set to 1 year
		final OffsetDateTime now = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
		final Date notBefore = Date.from(now.toInstant());
		final Date notAfter = Date.from(now.plusMonths(validityInMonths).toInstant());

		final X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
				name, serial, notBefore, notAfter, name, keyPair.getPublic());
		final ContentSigner contentSigner = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(keyPair.getPrivate());
		final JcaX509CertificateConverter converter = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider());

		return converter.getCertificate(certBuilder.build(contentSigner));
	}

	public record CreatedKeys(JWKSet jwkSet, List<String> certificates) {
	}
}
