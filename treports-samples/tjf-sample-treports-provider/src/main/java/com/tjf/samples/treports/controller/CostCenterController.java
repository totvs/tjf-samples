package com.tjf.samples.treports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.model.CostCenter;
import com.tjf.samples.treports.model.CostCenterRepository;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

@RestController
public class CostCenterController {
    
	@Autowired
	CostCenterRepository costCenterRepository;

	@GetMapping(path = "/api/ctb/v1/costcenters", produces = "application/json")
	public ApiCollectionResponse<CostCenter> getCostCenters(ApiFieldRequest fieldRequest, ApiPageRequest pageRequest,
			ApiSortRequest sortRequest) {
		return costCenterRepository.findAllProjected(fieldRequest, pageRequest, sortRequest);
	}
}
