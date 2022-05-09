package com.tjf.samples.treports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.model.Product;
import com.tjf.samples.treports.model.ProductRepository;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@GetMapping(path = "/api/ecommerce/v1/productbrands", produces = "application/json")
	public ApiCollectionResponse<Product> getCostCenters(ApiFieldRequest fieldRequest, ApiPageRequest pageRequest,
			ApiSortRequest sortRequest) {
		return productRepository.findAllProjected(fieldRequest, pageRequest, sortRequest);
	}
}
