package com.tjf.sample.github.apijpa.filter.controller;

import com.tjf.sample.github.apijpa.filter.model.AccountModel;
import com.tjf.sample.github.apijpa.filter.model.AccountModelRepository;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModel;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class SFController {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;

	@GetMapping(path = "/accounts")
	public Page<AccountModel> getAllAccounts(Pageable pageable, AccountModel simpleFilter) {
		return accountRepos.findAll(simpleFilter, pageable);
	}

	@GetMapping(path = "/employees")
	public Page<EmployeeModel> getAllEmployess(Pageable pageable, EmployeeModel simpleFilter) {
		System.out.println("TESTEEE NAME: " + simpleFilter.getName());
		System.out.println("TESTEEE ID: " + simpleFilter.getEmployeeId());
		return employeeRepos.findAll(simpleFilter, pageable);
	}

}
