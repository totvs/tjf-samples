package com.tjf.sample.github.corevalidation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.tjf.sample.github.coremoney.CoreMoneyApplication;
import com.tjf.sample.github.coremoney.model.AccountModel;

@SpringBootTest(classes = CoreMoneyApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void testGetRestMonetaryAmount() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("pt-BR")));

		ResponseEntity<AccountModel> response = restTemplate.getForEntity(path, AccountModel.class);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(Money.of(new BigDecimal(10), "BRL"), response.getBody().getPrice());
	}
	
	@Test
	public void testPostRestMonetaryAmount() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("pt-BR")));

		var value = Money.of(new BigDecimal(11.55), "USD");
		
		AccountModel account = new AccountModel(UUID.randomUUID(), value);

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);
		ResponseEntity<AccountModel> response = restTemplate.postForEntity(path, request, AccountModel.class);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(value, response.getBody().getPrice());

	}
}
