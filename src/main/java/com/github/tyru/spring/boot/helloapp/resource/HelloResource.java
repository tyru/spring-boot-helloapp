package com.github.tyru.spring.boot.helloapp.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tyru.spring.boot.helloapp.exception.HelloServiceExcepton;
import com.github.tyru.spring.boot.helloapp.resource.utils.ResponseUtils;

@RestController
// @LoggingAround
@RequestMapping(value = "/api/v1/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Scope("prototype")
public class HelloResource {
	private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);

	@Autowired
	private HelloService service;
	@Autowired
	@Qualifier("RawHyperSchema")
	private String rawHyperSchema;

	@GetMapping("schema")
	public String getRawHyperSchema() {
		return rawHyperSchema;
	}

	@GetMapping
	public List<HelloEntity> sayHelloInManyWays() {
		return service.getHelloMsgList();
	}

	@GetMapping(value = "{lang}")
	public ResponseEntity<HelloEntity> sayHelloIn(@PathVariable String lang) {
		try {
			return new ResponseEntity<HelloEntity>(service.getHelloMsg(lang), HttpStatus.OK);
		} catch (HelloServiceExcepton e) {
			logger.error(e.toString());
			return e.getStatus();
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<HelloEntity> addMsg(@RequestBody HelloEntity hello, HttpServletRequest request) {
		try {
			service.addMsg(hello);
			return ResponseUtils.created(request, hello.getLang());
		} catch (HelloServiceExcepton e) {
			logger.error(e.toString());
			return e.getStatus();
		}
	}

	@DeleteMapping("{lang}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMsg(@PathVariable String lang) {
		service.deleteMsg(lang);
	}
}
