package com.totvs.tjf.api.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;
import com.totvs.tjf.api.jpa.model.AccountModel;
import com.totvs.tjf.api.jpa.model.AccountModelRepository;
import com.totvs.tjf.api.jpa.model.EmployeeModel;
import com.totvs.tjf.api.jpa.model.EmployeeModelRepository;

import static com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;

@RestController
@ApiGuideline(ApiGuidelineVersion.v1)
@RequestMapping("/api/v1")
public class SFController {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;

	@GetMapping(path = "/accounts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse<AccountModel> getAllAccounts(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, AccountModel simpleFilter) {
		return accountRepos.findAllProjected(field, page, sort, simpleFilter);
	}

	@GetMapping(path = "/employees", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse<EmployeeModel> getAllEmployess(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, EmployeeModel simpleFilter) {
		return employeeRepos.findAllProjected(field, page, sort, simpleFilter);
	}

}