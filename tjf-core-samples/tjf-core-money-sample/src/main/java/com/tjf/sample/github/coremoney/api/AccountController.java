package com.tjf.sample.github.coremoney.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.coremoney.model.AccountModel;

@RestController
@RequestMapping(path = "/api/v1/sample", produces = APPLICATION_JSON_VALUE)
public class AccountController {

	@Autowired
	ObjectMapper mapper;
	
	@PostMapping("account")
	public AccountModel createAccount(@RequestBody AccountModel account) throws JsonProcessingException {
		System.out.println(mapper.writeValueAsString(account));
		return account;
	}

	@GetMapping("account")
	public AccountModel createAccount() throws JsonProcessingException {
		return new AccountModel(UUID.randomUUID(), Money.of(new BigDecimal(10), "BRL"));
	}
}
