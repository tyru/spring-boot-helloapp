package com.github.tyru.spring.boot.helloapp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import me.tyru.json.hyper.schema.HyperSchema;
import me.tyru.json.hyper.schema.HyperSchemaBuilder;

@SpringBootApplication
public class HelloApp {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HelloApp.class, args);
	}

	private static final String JSON_HYPER_SCHEMA_FILE = "/schemas/hello-schema.json";

	@Bean
	@Qualifier("RawHyperSchema")
	public String getRawHyperSchema() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(JSON_HYPER_SCHEMA_FILE)) {
			return IOUtils.toString(inputStream);
		}
	}

	@Bean
	@Qualifier("AbstractSpringJSONValidationFilter.hyperSchema")
	public HyperSchema injectHyperSchema() throws IOException {
		return HyperSchemaBuilder.createHyperSchema(JSON_HYPER_SCHEMA_FILE);
	}
}