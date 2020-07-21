package com.tjf.sample.github.corevalidation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.tjf.sample.github.corevalidation.model.AccountModel;

@SpringBootTest(classes = CoreValidationApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void testValidationError() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("pt-BR")));

		AccountModel account = new AccountModel(1, "Nome", "teste", 10.35d);

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);
		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(path, request, ErrorResponse.class);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Internal Server Error", response.getBody().getError());
		assertEquals("address: tamanho deve ser entre 10 e 100", response.getBody().getMessage());
		assertEquals(path, response.getBody().getPath());
		assertEquals(500, response.getBody().getStatus().intValue());
		assertNotNull(response.getBody().getTimestamp());

	}

	@Test
	public void testValidationErrorEN() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("en-US")));

		AccountModel account = new AccountModel(1, "Nome", "teste", 10.35d);

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);
		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(path, request, ErrorResponse.class);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Internal Server Error", response.getBody().getError());
		assertEquals("address: size must be between 10 and 100", response.getBody().getMessage());
		assertEquals(path, response.getBody().getPath());
		assertEquals(500, response.getBody().getStatus().intValue());
		assertNotNull(response.getBody().getTimestamp());

	}

	@Test
	public void testValidationSucess() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("pt-BR")));

		AccountModel account = new AccountModel(1, "Nome", "adress maior que 10 menor que 100", 10.35d);

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);
		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(path, request, ErrorResponse.class);

		assertEquals(200, response.getStatusCodeValue());
		assertNull(response.getBody());

	}
}
