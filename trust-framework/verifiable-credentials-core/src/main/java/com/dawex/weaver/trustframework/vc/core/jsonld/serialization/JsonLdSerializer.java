package com.dawex.weaver.trustframework.vc.core.jsonld.serialization;

import com.dawex.weaver.trustframework.vc.core.jsonld.CompositeValue;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdContexts;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdProperty;
import com.dawex.weaver.trustframework.vc.core.jsonld.annotation.JsonLdType;
import com.dawex.weaver.trustframework.vc.core.jsonld.exception.JsonLdSerializationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class JsonLdSerializer<T> extends JsonSerializer<T> {

	private static final String FIELD_CONTEXT = "@context";

	private static final String FIELD_TYPE = "@type";

	private static final String FIELD_NAME_FORMAT = "%s:%s";

	private final Class<T> serializableObjectClass;

	private final FormatProvider formatProvider;

	public JsonLdSerializer(Class<T> serializableObjectClass) {
		this.serializableObjectClass = serializableObjectClass;
		this.formatProvider = null;
	}

	public JsonLdSerializer(Class<T> serializableObjectClass, FormatProvider formatProvider) {
		this.serializableObjectClass = serializableObjectClass;
		this.formatProvider = formatProvider;
	}

	@Override
	public void serialize(T value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		writeContext(serializableObjectClass, jsonGenerator);
		writeType(serializableObjectClass, jsonGenerator);
		writeJsonLdProperties(value, serializableObjectClass, jsonGenerator);
		jsonGenerator.writeEndObject();
	}

	protected void writeContext(Class<?> targetClass, JsonGenerator jsonGenerator) throws IOException {
		if (targetClass == null || targetClass == Object.class) {
			return;
		}
		final JsonLdContexts jsonLdContexts = targetClass.getAnnotation(JsonLdContexts.class);
		if (jsonLdContexts == null) {
			// write context from parent class
			writeContext(targetClass.getSuperclass(), jsonGenerator);
		} else {
			// write context
			jsonGenerator.writeObjectField(FIELD_CONTEXT, jsonLdContexts);
		}
	}

	protected void writeType(Class<?> targetClass, JsonGenerator jsonGenerator) throws IOException {
		if (targetClass == null || targetClass == Object.class) {
			return;
		}
		final JsonLdType jsonLdType = targetClass.getAnnotation(JsonLdType.class);
		if (jsonLdType == null) {
			// write type from parent class
			writeType(targetClass.getSuperclass(), jsonGenerator);
		} else {
			// write type
			jsonGenerator.writeFieldName(FIELD_TYPE);
			if (jsonLdType.value().length == 1) {
				jsonGenerator.writeString(jsonLdType.value()[0]);
			} else {
				jsonGenerator.writeArray(jsonLdType.value(), 0, jsonLdType.value().length);
			}
		}
	}

	protected void writeJsonLdProperties(Object value, Class<?> targetClass, JsonGenerator jsonGenerator) {
		if (targetClass == null || targetClass == Object.class) {
			return;
		}
		// write parent class fields
		writeJsonLdProperties(value, targetClass.getSuperclass(), jsonGenerator);

		// get all fields annotated with @JsonLdProperty
		Arrays.stream(targetClass.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(JsonLdProperty.class))
				.map(field -> new FieldContext(field.getAnnotation(JsonLdProperty.class), getReadMethod(targetClass, field)))
				.filter(FieldContext::isValid)
				.forEach(context -> writeJsonLdProperty(value, context.jsonLdProperty, context.readMethod(), jsonGenerator));
	}

	private static Method getReadMethod(Class<?> targetClass, Field field) {
		try {
			if (targetClass.isRecord()) {
				return targetClass.getDeclaredMethod(field.getName());
			} else {
				return new PropertyDescriptor(field.getName(), targetClass, field.getName(), null).getReadMethod();
			}
		} catch (NoSuchMethodException | IntrospectionException e) {
			throw new JsonLdSerializationException(e);
		}
	}

	private void writeJsonLdProperty(Object obj, JsonLdProperty jsonLdProperty, Method getterMethod, JsonGenerator jsonGenerator) {
		try {
			final Object value = getterMethod.invoke(obj);

			if (value == null && !jsonLdProperty.mandatory()) {
				return;
			}
			jsonGenerator.writeObjectField(getJsonFieldName(jsonLdProperty), getJsonFieldValue(jsonLdProperty, value));

		} catch (IllegalAccessException | InvocationTargetException | IOException e) {
			throw new JsonLdSerializationException(e);
		}
	}

	private String getJsonFieldName(JsonLdProperty jsonLdProperty) {
		return jsonLdProperty.namespace().isEmpty()
				? jsonLdProperty.value()
				: FIELD_NAME_FORMAT.formatted(jsonLdProperty.namespace(), jsonLdProperty.value());
	}

	private Object getJsonFieldValue(JsonLdProperty jsonLdProperty, Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String valueString) {
			return getFormat(jsonLdProperty.formatName())
					.map(format -> format.formatted(valueString))
					.orElse(valueString);
		}
		if (value instanceof CompositeValue compositeValue) {
			return getFormat(jsonLdProperty.formatName())
					.map(format -> (Object) format.formatted(compositeValue.getValues()))
					.orElse(Arrays.stream(compositeValue.getValues()).map(String::valueOf).collect(Collectors.joining(",")));
		}
		if (value instanceof ZonedDateTime valueDateTime) {
			return valueDateTime.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
		return value;
	}

	private Optional<String> getFormat(String formatName) {
		if (formatProvider == null || formatName == null || formatName.isEmpty()) {
			return Optional.empty();
		}
		return formatProvider.getFormat(formatName);
	}

	/**
	 * Helper record used for serializing JSON-LD properties
	 */
	private record FieldContext(
			JsonLdProperty jsonLdProperty,
			Method readMethod
	) {
		public boolean isValid() {
			return readMethod != null;
		}
	}
}
