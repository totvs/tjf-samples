package com.tjf.sample.github.apicontext.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.apicontext.exception.JediNotFoundException;
import com.tjf.sample.github.apicontext.model.Jedi;
import com.tjf.sample.github.apicontext.model.JediRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class JediController {

	@Autowired
	private JediRepository jediRepo;

	@GetMapping
	public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return ApiCollectionResponse.from(jediRepo.findAllProjected(field, page, sort));
	}

	@GetMapping("/{id}")
	public Jedi getOne(@PathVariable(required = true) int id) {
		return jediRepo.findById(id).orElseThrow(() -> {
			throw new JediNotFoundException(id);
		});
	}

	@PostMapping
	public Jedi add(@RequestBody Jedi jedi) {
		return jediRepo.saveAndFlush(jedi);
	}

}
