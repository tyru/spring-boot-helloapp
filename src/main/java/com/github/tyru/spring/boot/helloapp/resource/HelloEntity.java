package com.github.tyru.spring.boot.helloapp.resource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="hello")
@lombok.Data
public class HelloEntity {
	@Id @NotNull
	private String lang;
	@NotNull
	private String msg;
}
