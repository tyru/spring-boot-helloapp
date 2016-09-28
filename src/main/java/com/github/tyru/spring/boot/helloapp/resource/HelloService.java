package com.github.tyru.spring.boot.helloapp.resource;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
		try {
			return repository.findByLang(lang);
		} catch (NoResultException e) {
			logger.error(e.toString());
			throw new HelloServiceExcepton(e, new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
	}

	public void addMsg(HelloEntity hello) throws HelloServiceExcepton {
		try {
			repository.save(hello);
		} catch (EntityExistsException e) {
			logger.error(e.toString());
			throw new HelloServiceExcepton(e, new ResponseEntity<>(HttpStatus.CONFLICT));
		}
	}

	public void deleteMsg(String lang) {
		repository.deleteByLang(lang);
	}
}
