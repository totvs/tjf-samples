package com.tjf.sample.github.apijpa.filter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.apijpa.filter.model.AccountModel;
import com.tjf.sample.github.apijpa.filter.model.AccountModelRepository;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModel;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModelRepository;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class SFController {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;

	@GetMapping(path = "/accounts")
	public ApiCollectionResponse<AccountModel> getAllAccounts(Pageable pageable, AccountModel simpleFilter) {
		return ApiCollectionResponse.from(accountRepos.findAll(simpleFilter, pageable).toList());
	}

	@GetMapping(path = "/employees")
	public ApiCollectionResponse<EmployeeModel> getAllEmployess(Pageable pageable, EmployeeModel simpleFilter) {
		return  ApiCollectionResponse.from(employeeRepos.findAll(simpleFilter, pageable).toList());
	}

}
