package com.tjf.sample.github.aggregate.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.aggregate.model.AccountModel;
import com.tjf.sample.github.aggregate.repository.AccountRepository;

@RestController
@RequestMapping(path = "/api/v1/sample", produces = { APPLICATION_JSON_VALUE })
public class AccountController {

	@Autowired
	private AccountRepository repository;

	@PostMapping("account")
	@Transactional
	public void postAccount(@RequestBody AccountModel account) {

		this.repository.insert(account);
	}
}
