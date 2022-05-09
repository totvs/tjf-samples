package com.tjf.samples.treports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.model.RetailStockLevel;
import com.tjf.samples.treports.model.RetailStockLevelRepository;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
public class RetailStockLevelController {

	@Autowired
	RetailStockLevelRepository retailStockLevelRepository;

	@GetMapping(path = "/api/retail/v1/retailStockLevel", produces = "application/json")
	public ApiCollectionResponse<RetailStockLevel> getCostCenters(ApiFieldRequest fieldRequest,
			ApiPageRequest pageRequest, ApiSortRequest sortRequest) {
		return retailStockLevelRepository.findAllProjected(fieldRequest, pageRequest, sortRequest);
	}
}
