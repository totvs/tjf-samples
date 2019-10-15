package br.com.star.wars.controller;

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

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import br.com.star.wars.model.Droid;
import br.com.star.wars.model.DroidSpecification;
import br.com.star.wars.repository.DroidRepository;

@RestController
@RequestMapping(path = DroidController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class DroidController {

	public static final String PATH = "api/v1/droid";

	@Autowired
	DroidRepository droidRepository;

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

		return droidRepository.findAll(pageRequest, specs);
	}

	@GetMapping(path = "/findLike/{function}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidLikeDescription(@PathVariable("function") String function) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.functionLike(function));

		return droidRepository.findAll(pageRequest, specs);
	}

	@GetMapping(path = "/findBetween")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidBetweenHeight(@RequestHeader(name = "from") double from,
			@RequestHeader(name = "util") double util) {
		ApiPageRequest pageRequest = new ApiPageRequest();

		Specification<Droid> specs = Specification.where(DroidSpecification.heightBetween(from, util));

		return droidRepository.findAll(pageRequest, specs);
	}

	@GetMapping(path = "/findExists/{height}")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Droid> findDroidExists(@PathVariable("height") double height) {
		ApiPageRequest pageRequest = new ApiPageRequest();
		Specification<Droid> specs = Specification.where(DroidSpecification.droidExists(height));

		return droidRepository.findAll(pageRequest, specs);
	}

}