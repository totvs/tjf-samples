package com.tjf.sample.github.apijpa.filter.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
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
	public ApiCollectionResponse<AccountModel> getAllAccounts(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, AccountModel simpleFilter) {
		return ApiCollectionResponse.from(accountRepos.findAllProjected(field, page, sort, simpleFilter));
	}

	@GetMapping(path = "/employees")
	public ApiCollectionResponse<EmployeeModel> getAllEmployess(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, EmployeeModel simpleFilter) {
		return ApiCollectionResponse.from(employeeRepos.findAllProjected(field, page, sort, simpleFilter));
	}

}
