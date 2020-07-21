package com.tjf.sample.github.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.model.Jedi;
import com.tjf.sample.github.model.JediRepository;
import com.totvs.tjf.api.context.v2.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v2.request.ApiPageRequest;
import com.totvs.tjf.api.context.v2.request.ApiSortRequest;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = "/jedi/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class JediController {

	@Autowired
	private JediRepository jediRepository;

	@GetMapping("jedis")
	public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return ApiCollectionResponse.from(jediRepository.findAllProjected(field, page, sort));
	}

}