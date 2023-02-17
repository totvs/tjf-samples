package com.tjf.sample.github.securityweb.requestvalidation.config;

import com.totvs.tjf.security.requestvalidation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {

	@Bean
	Validator validator() {
		return new OrganizationValidator();
	}
}
