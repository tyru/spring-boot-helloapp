package com.github.tyru.spring.boot.helloapp.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tyru.spring.boot.helloapp.exception.HelloServiceExcepton;

@Service
public class HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);
	@Autowired
	private HelloRepository repository;

	public List<HelloEntity> getHelloMsgList() {
		return repository.findAll();
	}

	public HelloEntity getHelloMsg(String lang) throws HelloServiceExcepton {
		final HelloEntity result = repository.findByLang(lang);
		if (result == null) {
			logger.info("Not found with " + lang);
			throw new HelloServiceExcepton(null, new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return result;
	}

	@Transactional
	public void addMsg(HelloEntity hello) throws HelloServiceExcepton {
		if (repository.exists(hello.getLang())) {
			throw new HelloServiceExcepton(null, new ResponseEntity<>(HttpStatus.CONFLICT));
		}
		repository.save(hello);
	}

	@Transactional
	public void deleteMsg(String lang) {
		repository.deleteByLang(lang);
	}
}
