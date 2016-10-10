package com.github.tyru.spring.boot.helloapp.filter;

import org.springframework.web.bind.annotation.ControllerAdvice;

import me.tyru.json.hyper.schema.filter.AbstractSpringJSONValidationFilter;

@ControllerAdvice
public class SpringJSONValidationFilter extends AbstractSpringJSONValidationFilter {}
