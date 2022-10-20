package com.dawex.weaver.trustframework.vc.core.jsonld.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonLdProperty {
	String value() default "";

	String namespace() default "";

	String formatName() default "";

	boolean mandatory() default false;
}
