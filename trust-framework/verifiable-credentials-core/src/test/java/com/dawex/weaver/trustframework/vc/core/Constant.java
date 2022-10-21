package com.dawex.weaver.trustframework.vc.core;

public class Constant {

	public static final String CERTIFICATE = """
			-----BEGIN CERTIFICATE-----
			MIICoDCCAYigAwIBAgIIf0tKMjVem+MwDQYJKoZIhvcNAQELBQAwEDEOMAwGA1UEAwwFRGF3ZXgwHhcNMjIwODMxMDAwMDAwWhcNMjMwODMxMDAwMDAwWjAQMQ4wDAYDVQQDDAVEYXdleDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANU5dflks7EshSmFV7M555RrvduBbgGfKvBrkLOjMSEjljriAoOAX+9TSEgwnIuyp7H3KOZ77XxcXPmTWnPT6WzgHZ94KwxWuV62wdZqwRZyU+1Wu8regQ42BGMKbfNFpJjm3aBSuhCVHDZryOYYb3kuXeXVmsNgSztM52Qzl8KTQi+Xh/FIv+BlN1RptGrPzOqOVkazhSWtsQGHidEo11eDTR3qpY/o+z+Kl/PclY0YRfQySX68MGMWJu1pop+bOCobcOi5YBIVD3KGxEqNM/tkRfsiqYXaIjc2CCHRhLElPQ36/To8e7ZIg84PqnGg3SKJCUzNx4nhuMjp1BdPO68CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAqg6LSLN6Afx29gZ+r912+omxhW6NQMRSfB0qgOVl3dA2H96FMoeZKrY4UxlI/h3AMEdslQ3LbqAIMry1HqZzHH88FxHo87z7UBhP7MwIinNAwGDf1J1kIlQFfDa2Zgax2btPXwP2rZxQz0+UIFVeTjNNT7gaXUZyX5X2FpJe7H493hjdpkvpgP+kxh16iV5W2wpsRPocjnyIvoxRGK8rjy8zXtUAbWwKxV9Pad/GWteybRy8lHu3MluzTeGibvGZjDBkqZ+uKPdxv1TMCu61J7eZ3eR+F3ZYJPQjnYKAyGNVV4VD4RpG/Bc7bTQ/YJ6yzw/ngm+1rPIoA36OQ9OKVA==
			-----END CERTIFICATE-----""";

	public static final String JWK_SET = """
			{
			  "keys": [
			    {
			      "d": "BDcEEX9_m9d89GKp5qWU4da1ZJLs6URLtX_y1tI5hOWusRBJq27Mtq7nRWIMnihUb0yq9s9pVOWAgwB6L8L3IMtXWpEmDLLkLC6hq0rl8TsmVJE72hlG5NCFiq2omb_9LnaV1_b6rZw8-Ahuarn97h_QBFySGB2drxjolA7_B34XctmgXsNe-ph2JnjYVeMK_R3QdRn4SVx5nyZ2R8lLzPHfAuJlMB0zjlyReB5fcIBc-JNcKSj2Xc5eFEkahsNAk1XShVoW98-yRwvQ0XSE0xRNUE9uwZd3q5C3vW3-TIXqnMV0I8dPvpxc48daC0PGq7t4ScZ0IFWQNfHDdpon7Q",
			      "dp": "fDnE5cplPat-uZ7tPOATnXYWayhBugESZiddajTwJZAUoN_cvS_qoibOGPNwDQr69ZhPBeZ24-1Rw2Te4r1l48lcbie3q1IPmAcJJqd_ccXKyex8jjpCNhAyc5LAjwEtT2N4EqO8z860avd-L9s56hTIOP36zG9n8pxAT0h8m_E",
			      "dq": "T8Ft0aIPwTcL6vfYLvUFGGdFtorqMv2AA9qF1N3FCZke1PKBBPA9IlZVTZAz3TTxkXCw9MbwDa4fKePW-1AYv565wcnYtRMTDQE2PwXcstnALwHCGt1ApkAzEaOG3084_gxRG5YwgWauCoUyY7MG5JBAkMMHQAs9XhE5OCcjEP0",
			      "e": "AQAB",
			      "kid": "9172506645574360035",
			      "kty": "RSA",
			      "n": "1Tl1-WSzsSyFKYVXsznnlGu924FuAZ8q8GuQs6MxISOWOuICg4Bf71NISDCci7Knsfco5nvtfFxc-ZNac9PpbOAdn3grDFa5XrbB1mrBFnJT7Va7yt6BDjYEYwpt80WkmObdoFK6EJUcNmvI5hhveS5d5dWaw2BLO0znZDOXwpNCL5eH8Ui_4GU3VGm0as_M6o5WRrOFJa2xAYeJ0SjXV4NNHeqlj-j7P4qX89yVjRhF9DJJfrwwYxYm7Wmin5s4Khtw6LlgEhUPcobESo0z-2RF-yKphdoiNzYIIdGEsSU9Dfr9Ojx7tkiDzg-qcaDdIokJTM3HieG4yOnUF087rw",
			      "p": "11uI7BYRxQJi5wlyFuXBhUC8wE8ueDs9_0Dl0UN9Zy0dZVz5Sh1cdlN76_dI5QPD3t4XVkFd0ZnrlMezjsA21Q_Y7-lJSoc9CpTuyLg7w0CGdVYZevCy4qB3pF9f34Hbi2wnsmTsOS10IjXbX7VOSl9O1d4O9-p35qzH-xNW5fU",
			      "q": "_Xbe19oUjGlVdSjhhXbzbhECLrvbU_0oQ0Ps00eznyYES8qLk0UFt7HWYyGo0nNvLxOrKBh9lkKl_8n8Q-rlp3UUlrM9Cc3mIV80nPaJgWiJwRw-OfCzM4dA8pmw1ZhfThmP7QXV78Ch5Hs1BMJ74sq1Z6a4HKHx3UXuY6nWcJM",
			      "qi": "OqI_tvrbiSd7oFRXChNkKborIzBRmarsVzzEVsAowrkICiaDiIjyLMzgsJuVUXWCRP57xozJVj7bkODj3-hcQ8PcetJkcEbXfOOhMZO9WlA8139iZurB-xkeJOauAZSVPJ31QextWpR3lHGZb-Vv4w0MCeQ_dY4wlfozD9QUC4k",
			      "x5c": [
			        "MIICoDCCAYigAwIBAgIIf0tKMjVem+MwDQYJKoZIhvcNAQELBQAwEDEOMAwGA1UEAwwFRGF3ZXgwHhcNMjIwODMxMDAwMDAwWhcNMjMwODMxMDAwMDAwWjAQMQ4wDAYDVQQDDAVEYXdleDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANU5dflks7EshSmFV7M555RrvduBbgGfKvBrkLOjMSEjljriAoOAX+9TSEgwnIuyp7H3KOZ77XxcXPmTWnPT6WzgHZ94KwxWuV62wdZqwRZyU+1Wu8regQ42BGMKbfNFpJjm3aBSuhCVHDZryOYYb3kuXeXVmsNgSztM52Qzl8KTQi+Xh/FIv+BlN1RptGrPzOqOVkazhSWtsQGHidEo11eDTR3qpY/o+z+Kl/PclY0YRfQySX68MGMWJu1pop+bOCobcOi5YBIVD3KGxEqNM/tkRfsiqYXaIjc2CCHRhLElPQ36/To8e7ZIg84PqnGg3SKJCUzNx4nhuMjp1BdPO68CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAqg6LSLN6Afx29gZ+r912+omxhW6NQMRSfB0qgOVl3dA2H96FMoeZKrY4UxlI/h3AMEdslQ3LbqAIMry1HqZzHH88FxHo87z7UBhP7MwIinNAwGDf1J1kIlQFfDa2Zgax2btPXwP2rZxQz0+UIFVeTjNNT7gaXUZyX5X2FpJe7H493hjdpkvpgP+kxh16iV5W2wpsRPocjnyIvoxRGK8rjy8zXtUAbWwKxV9Pad/GWteybRy8lHu3MluzTeGibvGZjDBkqZ+uKPdxv1TMCu61J7eZ3eR+F3ZYJPQjnYKAyGNVV4VD4RpG/Bc7bTQ/YJ6yzw/ngm+1rPIoA36OQ9OKVA=="
			      ],
			      "x5t#S256": "rxHXEEmEE1vz1ynTGFGZmG38l-JSA1fYskDCD69x9Sg"
			    }
			  ]
			}""";

	public static final String JSON_LD = """
			{
			  "@context" : {
			    "@base" : "https://dawex.com",
			    "sd" : "https://www.w3.org/2018/credentials/v1",
			    "gax-participant" : "https://w3id.org/gaia-x/participant#"
			  },
			  "@type" : [ "VerifiableCredential", "LegalPerson" ],
			  "@id" : "./api/secure/participant/organisations/62b573deb33e417edcb34-id/verifiableCredential",
			  "sd:issuer" : "./organisations/62b573deb33e417ed-issuer",
			  "sd:issuanceDate" : "2022-07-28T15:16:01Z",
			  "sd:credentialSubject" : {
			    "@id" : "./organisations/62b573deb33e417e-company",
			    "gax-participant:name" : "Mercat de la Boqueria",
			    "gax-participant:registrationNumber" : "AB-1234-YZ",
			    "gax-participant:headquarterAddress" : {
			      "gax-participant:street-address" : "La Rambla, 91",
			      "gax-participant:postal-code" : "08001",
			      "gax-participant:region" : "Cataluña",
			      "gax-participant:locality" : "Barcelona",
			      "gax-participant:country-name" : "ESP"
			    },
			    "gax-participant:legalAddress" : {
			      "gax-participant:street-address" : "7 rue Grenette",
			      "gax-participant:postal-code" : "74000",
			      "gax-participant:region" : "Savoie",
			      "gax-participant:locality" : "Annecy",
			      "gax-participant:country-name" : "FRA"
			    }
			  }
			}""";

	private Constant() {
		// no instance allowed
	}
}
