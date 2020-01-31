package com.tjf.sample.github.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tjf.sample.github.validation.model.AccountModel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AccountApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void testValidationError() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AccountModel account = new AccountModel(1, "Nome", "teste", new Double("10.35"));

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);

		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(path, request, ErrorResponse.class);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Internal Server Error", response.getBody().getError());
		assertEquals("address: tamanho deve estar entre 10 e 100", response.getBody().getMessage());
		assertEquals(path, response.getBody().getPath());
		assertEquals(500, response.getBody().getStatus().intValue());
		assertNotNull(response.getBody().getTimestamp());
	}

	@Test
	public void testValidationSucess() {

		String path = "/api/v1/sample/account";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		AccountModel account = new AccountModel(1, "Nome", "adress maior que 10 menor que 100", new Double("10.35"));

		HttpEntity<AccountModel> request = new HttpEntity<>(account, headers);

		ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(path, request, ErrorResponse.class);

		assertEquals(200, response.getStatusCodeValue());
		assertNull(response.getBody());
	}
}
