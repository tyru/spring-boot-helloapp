package com.github.tyru.spring.boot.helloapp.exception;

import org.springframework.http.ResponseEntity;

import com.github.tyru.spring.boot.helloapp.resource.HelloEntity;

@lombok.Getter
public class HelloServiceExcepton extends Exception {
	private Exception exception;
	private ResponseEntity<HelloEntity> status;

	public HelloServiceExcepton(Exception exception, ResponseEntity<HelloEntity> status) {
		this.exception = exception;
		this.status = status;
	}
}
