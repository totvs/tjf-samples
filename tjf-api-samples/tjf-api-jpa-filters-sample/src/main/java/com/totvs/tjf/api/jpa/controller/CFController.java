package com.totvs.tjf.api.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.v1.request.ApiComplexFilterRequest;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;
import com.totvs.tjf.api.jpa.complexfilter.ComplexFilterRepository;
import com.totvs.tjf.api.jpa.model.AccountModel;
import com.totvs.tjf.api.jpa.model.EmployeeModel;

import static com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;

@RestController
@ApiGuideline(ApiGuidelineVersion.v1)
@RequestMapping("/api/v1")
public class CFController {

	@Autowired
	private ComplexFilterRepository complexFilterRepos;
	
	@GetMapping(path = "/contas", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse <JsonNode> getContas(ApiComplexFilterRequest filter,
			ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return complexFilterRepos.getResponse(AccountModel.class, filter, field, page, sort);
	}

	@GetMapping(path = "/empregados", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse <JsonNode> getEmpregados(ApiComplexFilterRequest filter,
			ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return complexFilterRepos.getResponse(EmployeeModel.class, filter, field, page, sort);
	}
	
}