package com.dawex.weaver.trustframework.vc.core.jsonld.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A JSON-LD context, mapping a term to an Internationalized Resource Identifiers (IRIs).
 * <a href="https://www.w3.org/TR/json-ld/#the-context">JSON-LD: Context</a>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonLdContext {

	String term();

	String iri();
}
