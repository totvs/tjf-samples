package com.tjf.sample.github.coremoney;

import javax.money.MonetaryAmount;

import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

import com.totvs.tjf.core.money.MonetaryAmountApiRepresentation;

import jakarta.annotation.PostConstruct;

@Configuration
public class SwaggerConfiguration {

	@PostConstruct
	private void init() {
		SpringDocUtils.getConfig().replaceWithClass(MonetaryAmount.class, MonetaryAmountApiRepresentation.class);
	}
}