package com.github.tyru.spring.boot.helloapp.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends JpaRepository<HelloEntity, String> {
	HelloEntity findByLang(String lang);
	void deleteByLang(String lang);
}
