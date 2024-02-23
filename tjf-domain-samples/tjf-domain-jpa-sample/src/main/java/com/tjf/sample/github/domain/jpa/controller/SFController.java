package com.tjf.sample.github.domain.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.domain.jpa.model.AccountModel;
import com.tjf.sample.github.domain.jpa.model.AccountModelRepository;
import com.tjf.sample.github.domain.jpa.model.EmployeeModel;
import com.tjf.sample.github.domain.jpa.model.EmployeeModelRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class SFController {

	@Autowired
	private AccountModelRepository accountRepos;

	@GetMapping(path = "/accounts")
	public Page<AccountModel> getAllAccounts(Pageable pageable) {
		return accountRepos.findAll(pageable);
	}
}
