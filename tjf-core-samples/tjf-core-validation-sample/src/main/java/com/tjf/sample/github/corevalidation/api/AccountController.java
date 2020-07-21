package com.tjf.sample.github.corevalidation.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.corevalidation.exception.AccountException;
import com.tjf.sample.github.corevalidation.model.AccountModel;
import com.totvs.tjf.core.validation.ValidatorService;

@RestController
@RequestMapping(path = "/api/v1/sample", produces = APPLICATION_JSON_VALUE)
public class AccountController {

	@Autowired
	private ValidatorService validator;

	@PostMapping("account")
	public void createAccount(@RequestBody AccountModel account) {
		validator.validate(account).ifPresent(violations -> {
			throw new AccountException(violations);
		});
	}

}
