# Gaia-X Library 

Gaia-X is a project initiated by Europe where representatives from business, politics, and science from Europe and around the globe are working together, hand in hand, to create a federated and secure data infrastructure. 

The Gaia-X Trust Framework is the set of rules that define the minimum baseline to be part of the Gaia-X Ecosystem. Those rules provide a common governance and the basic level of interoperability across individual ecosystems while letting the users in full control of their choices.

The Trust Framework foresees Verifiable Credentials (VC) and linked data representations as cornerstone of its future operations.

Gaia-X Self-Descriptions are:
- machine readable texts
- cryptographically signed, preventing tampering with its content
- following the Linked Data principles to describe attributes

The format is following the W3C Verifiable Credentials Data Model.

## Description

### Verifiable credential core

The core library handles base operations for generating and signing verifiable credentials: 
- JSON-LD serialization made easier by providing annotations and Jackson serializers
- Generating / validating a JWS
- Creation of RSA key pairs and X.509 self-signed certificates
- Adding a proof to an existing VC

### Verifiable credential model

The model library regroups the java objects representing the Gaia-X core concepts, with their JSON-LD serialization configuration. 
It is based on the core library, and is the main entry point supposing there is no need for custom configuration.

## Usage

### Installation

The library is built using Java 17, which is the latest Java LTS version available.

Maven

```xml
<!-- Java 17 -->
<dependency>
    <groupId>com.dawex.weaver</groupId>
    <artifactId>verifiable-credential-model</artifactId>
    <version>1-SNAPSHOT</version>
</dependency>
```

### Configuration

#### Configure the `FormatProvider` for serializing identifiers

The format of identifiers is configurable through a format provider, that maps a format name to a format String following
the [Formatter syntax](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Formatter.html#syntax).
The arguments given to the Formatter is the field value. If no format is defined for a particular format name, then the raw value is
returned.

Here is an example of how to configure the `DefaultFormatProvider`:

```java
final DefaultFormatProvider formatProvider = new DefaultFormatProvider();
formatProvider.setFormat(Format.ORGANISATION_VERIFIABLE_CREDENTIAL,"./organisations/%s/verifiableCredential");
```

The following format names are available:

| Format name | Applies to   | Description |
| --- |--------------| --- |
| DATA_PRODUCT_COPYRIGHT_OWNED_BY | Data product | copyrightOwnedBy |
| DATA_PRODUCT_CREDENTIAL_SUBJECT | Data product | credential subject identifier |
| DATA_PRODUCT_ISSUER | Data product | verifiable credential issuer |
| DATA_PRODUCT_PROVIDED_BY | Data product | credential subject providedBy |
| DATA_PRODUCT_VERIFIABLE_CREDENTIAL | Data product | verifiable credential id |
| ORGANISATION_CREDENTIAL_SUBJECT | Organisation | credential subject identifier |
| ORGANISATION_ISSUER | Organisation | verifiable credential issuer |
| ORGANISATION_VERIFIABLE_CREDENTIAL | Organisation | verifiable credential id |

#### Configure the `ObjectMapper` for serializing organisation verifiable credentials

Two Jackson modules are preconfigured to handle organisation and data product JSON-LD serialization. They require a `FormatProvider` 
(configured in the previous section), and the base IRI that will be used as the base context of the JWON-LD document. 

Here is an example of how to configure the `ObjectMapper` for generating both organisation and data product verifiable credentials:
```java
final var objectMapper = new ObjectMapper();
// for serializing organisation verifiable credentials
objectMapper.registerModule(organisationSerializationModule(getFormatProvider(), () -> "https://mycompany.com"));
// for serializing data product verifiable credentials
objectMapper.registerModule(dataProductSerializationModule(getFormatProvider(), () -> "https://mycompany.com"));
```

#### Configure the JSON Web Key (JWK) 

If necessary, the library also provide a way to generate : 
- a JWK containing a key pair (private and public RSA keys)
- a self-signed X.509 certificate

```java
// Generate a key pair and a X.509 self-signed certificate (with common name "My Company", and a 12 months validity) 
final JwkSetUtils.CreatedKeys createdKeys = JwkSetUtils.createKeys("My Company", 12);
// Get the JWK
final JWK jwk = CREATED_KEYS.jwkSet().getKeys().stream().findFirst().orElseThrow();
// Get the X.509 certificate
final String certificate = CREATED_KEYS.certificates().stream().findFirst().orElseThrow();
final X509Certificate certificate = com.nimbusds.jose.util.X509CertUtils.parse(Constant.CERTIFICATE);
```

### Generate a verifiable credential

With the configuration defined in the previous section, we can now serialize a verifiable credential POJO in JSON-LD, and add a proof 
 JWK (using the private key):

```java
// Build the verifiableCredential POJO
final var verifiableCredential = OrganisationVerifiableCredential.builder()
        .id("6f77c1344-8b25-4a9a-b0ae-ebe9bd2af61d")
        .issuer("4f21b0c9-63f2-4c5d-88db-239e2389687a")
        .issuanceDate(LocalDate.of(2022, Month.JULY, 28).atTime(15, 16, 1).atZone(ZoneOffset.UTC))
        .organisationCredentialSubject(OrganisationCredentialSubject.builder()
            .id("640cf1a8-f43d-4b2b-822e-8770585e2182")
            .name("My Company")
            .registrationNumber("1234567890")
            // ...
            .build())
        .build();

// Generate the proof
final var proof = ProofGenerator.generateProof(
		objectMapper.writeValueAsString(verifiableCredential),
		"https://mycompany.com/jwks",
		jwk);
// Get a signed VC, by adding the proof to the verifiableCredential POJO 
final var signedVc = new SignedObject<>(verifiableCredential, proof);
// Serialize the signed VC to get it as a JSON-LD String
final var serializedVc = objectMapper.writeValueAsString(signedVc);
```

## Contributing

You may contribute to this test suite by submitting pull requests here:

https://github.com/dawex/Weaver

## License

[Apache License, Version 2.0](LICENSE)
