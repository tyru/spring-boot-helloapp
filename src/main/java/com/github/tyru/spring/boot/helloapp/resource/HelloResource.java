package com.github.tyru.spring.boot.helloapp.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.tyru.spring.boot.helloapp.exception.HelloServiceExcepton;

@RestController
// @LoggingAround
@RequestMapping(value = "hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Scope("prototype")
public class HelloResource {
	private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);

	@Autowired
	private HelloService service;
	@Autowired
	@Qualifier("RawHyperSchema")
	private String rawHyperSchema;

	@RequestMapping(method = RequestMethod.GET, value = "schema")
	public String getRawHyperSchema() {
		return rawHyperSchema;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<HelloEntity> sayHelloInManyWays() {
		return service.getHelloMsgList();
	}

	@RequestMapping(method = RequestMethod.GET, value = "{lang}")
	public ResponseEntity<HelloEntity> sayHelloIn(@PathVariable String lang) {
		try {
			return new ResponseEntity<HelloEntity>(service.getHelloMsg(lang), HttpStatus.OK);
		} catch (HelloServiceExcepton e) {
			logger.error(e.toString());
			return e.getStatus();
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<HelloEntity> addMsg(@RequestBody HelloEntity hello) {
		try {
			service.addMsg(hello);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (HelloServiceExcepton e) {
			logger.error(e.toString());
			return e.getStatus();
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "{lang}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMsg(@PathVariable String lang) {
		service.deleteMsg(lang);
	}
}
