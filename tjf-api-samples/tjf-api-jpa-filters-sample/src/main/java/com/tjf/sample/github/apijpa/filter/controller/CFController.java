package com.tjf.sample.github.apijpa.filter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.tjf.sample.github.apijpa.filter.model.AccountModel;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModel;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.request.ApiComplexFilterRequest;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;
import com.totvs.tjf.api.jpa.complexfilter.ComplexFilterRepository;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class CFController {

	@Autowired
	private ComplexFilterRepository complexFilterRepos;

	@GetMapping(path = "/contas")
	public ApiCollectionResponse<JsonNode> getContas(ApiComplexFilterRequest filter, ApiFieldRequest field,
			ApiPageRequest page, ApiSortRequest sort) {
		var response = complexFilterRepos.getResponse(AccountModel.class, filter, field, page, sort);
		return ApiCollectionResponse.from(response);
	}

	@GetMapping(path = "/empregados")
	public ApiCollectionResponse<JsonNode> getEmpregados(ApiComplexFilterRequest filter, ApiFieldRequest field,
			ApiPageRequest page, ApiSortRequest sort) {
		var response = complexFilterRepos.getResponse(EmployeeModel.class, filter, field, page, sort);
		return ApiCollectionResponse.from(response);
	}

}
