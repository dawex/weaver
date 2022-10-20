package com.dawex.weaver.trustframework.vc.core.jose;

import com.dawex.weaver.trustframework.vc.core.Constant;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwkSetUtilsTest {

	@Test
	void shouldParseJwkSet() throws ParseException {
		final JWKSet jwkSet = JWKSet.parse(Constant.JWK_SET);
		final Map<String, Object> jwkSetAsMap = jwkSet.toJSONObject(false);
		final String expectedJwkSet = jwkSet.toString(false);

		final JWKSet actual = JwkSetUtils.parseJwkSet(jwkSetAsMap);

		assertThat(actual.toString(false))
				.isEqualTo(expectedJwkSet);
	}
}