package com.github.tyru.spring.boot.helloapp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import me.tyru.json.hyper.schema.HyperSchema;
import me.tyru.json.hyper.schema.JSONRequest;

@ControllerAdvice
public class ValidateJSONInterceptor extends RequestBodyAdviceAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ValidateJSONInterceptor.class);
	@Autowired
	private HyperSchema hyperSchema;

	@Override
	public boolean supports(MethodParameter paramMethodParameter, Type paramType,
			Class<? extends HttpMessageConverter<?>> paramClass) {
		return true;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
		// Throws a ValidationException if accepted json is invalid
		logger.info("Start validation");
		hyperSchema.validate(SpringJSONRequest.of(inputMessage));
		logger.debug("Validation succeeded");
		return super.beforeBodyRead(inputMessage, methodParameter, targetType, selectedConverterType);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> handleValidationException(ValidationException e) {
		logger.error(e.toString());
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	static private class SpringJSONRequest implements JSONRequest {
		private final HttpInputMessage inputMessage;

		public static SpringJSONRequest of(HttpInputMessage inputMessage) {
			return new SpringJSONRequest(inputMessage);
		}
		private SpringJSONRequest(HttpInputMessage inputMessage) {
			if (!(inputMessage instanceof HttpRequest)) {
				throw new IllegalArgumentException(
						"Given instance MUST have implemented both of HttpInputMessage and HttpRequest.");
			}
			this.inputMessage = inputMessage;
		}

		@Override
		public String getMethod() {
			return ((HttpRequest) inputMessage).getMethod().toString();
		}

		@Override
		public String getHref() {
			return ((HttpRequest) inputMessage).getURI().getPath();
		}

		@Override
		public String getEncType() {
			final MediaType mediaType = inputMessage.getHeaders().getContentType();
			return mediaType.getType() + "/" + mediaType.getSubtype();
		}

		@Override
		public String getEntityWithKeepingStream(String charset) {
			try {
				return IOUtils.toString(inputMessage.getBody());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		@Override
		public MultivaluedMap<String, String> getQueryParameters() {
			final URI uri = ((HttpRequest) inputMessage).getURI();
		    return Arrays.stream(uri.getQuery().split("&"))
		            .map(this::splitQueryParameter)
		            .collect(Collectors.groupingBy(
		            		SimpleImmutableEntry::getKey,
		            		MultivaluedHashMap::new,
		            		Collectors.mapping(MultivaluedHashMap.Entry::getValue, Collectors.toList())));
		}

		private SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
		    final int idx = it.indexOf("=");
		    final String key = idx > 0 ? it.substring(0, idx) : it;
		    final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
		    return new SimpleImmutableEntry<>(key, value);
		}
	}
}
