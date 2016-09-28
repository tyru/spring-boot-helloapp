package com.github.tyru.spring.boot.helloapp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.MappedInterceptor;

import me.tyru.json.hyper.schema.HyperSchema;
import me.tyru.json.hyper.schema.HyperSchemaBuilder;

@SpringBootApplication
public class HelloApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloApp.class, args);
    }

    @Bean
    @Qualifier("RawHyperSchema")
	public String getRawHyperSchema() throws IOException {
    	try (InputStream inputStream = getClass().getResourceAsStream("/schemas/hello-schema.json")) {
			return IOUtils.toString(inputStream);
		}
	}

    @Bean
	public HyperSchema getHyperSchema(@Autowired @Qualifier("RawHyperSchema") String rawHyperSchema) throws IOException {
    	return HyperSchemaBuilder.hyperSchema(new JSONObject(rawHyperSchema)).build();
	}

    @Bean
    public MappedInterceptor interceptor(@Autowired ValidateJSONInterceptor interceptor) {
        return new MappedInterceptor(new String[]{"/**"}, interceptor);
    }
}