package com.github.tyru.spring.boot.helloapp.resource.utils;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriTemplate;

public interface ResponseUtils {

	static <T> ResponseEntity<T> created(HttpServletRequest request, String id) {
	    URI uri = new UriTemplate("{requestUrl}/{username}").expand(request.getRequestURI(), id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri.toASCIIString());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

}
