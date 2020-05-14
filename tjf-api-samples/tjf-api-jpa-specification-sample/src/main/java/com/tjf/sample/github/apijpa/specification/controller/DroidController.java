package com.tjf.sample.github.apijpa.specification.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.apijpa.specification.model.Droid;
import com.tjf.sample.github.apijpa.specification.model.DroidSpecification;
import com.tjf.sample.github.apijpa.specification.repository.DroidRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = DroidController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class DroidController {

	public static final String PATH = "api/v1/droid";

	@Autowired
	private DroidRepository droidRepository;

	@GetMapping(path = "/findAll")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Droid> findAllDroids() {
		return droidRepository.findAll();
	}

	@GetMapping(path = "/findByName/{name}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidByName(@PathVariable("name") String name) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.nameEq(name));
		return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
	}

	@GetMapping(path = "/findLike/{function}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidLikeDescription(@PathVariable("function") String function) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.functionLike(function));
		return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
	}

	@GetMapping(path = "/findBetween")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidBetweenHeight(@RequestHeader(name = "from") double from,
			@RequestHeader(name = "util") double util) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.heightBetween(from, util));
		return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
	}

	@GetMapping(path = "/findExists/{height}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidExists(@PathVariable("height") double height) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.droidExists(height));
		return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
	}

}