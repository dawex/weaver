package com.dawex.weaver.trustframework.vc.core.jsonld;

/**
 * An object composed of multiple values.
 * It allows to use a JsonLdProperty with a pattern requiring multiple formatting objects;
 * the result of <code>getValues</code> will be applied to the pattern.
 */
public interface CompositeValue {
	Object[] getValues();
}
