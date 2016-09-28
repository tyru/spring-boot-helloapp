package com.github.tyru.spring.boot.helloapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import me.tyru.json.hyper.schema.HyperSchema;

@Component
public class ValidateJSONInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ValidateJSONInterceptor.class);
	@Autowired
	private HyperSchema hyperSchema;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("Start validation.");
		// Throws a ValidationException if accepted json is invalid
		hyperSchema.validate(request);
		return true;
	}
}
