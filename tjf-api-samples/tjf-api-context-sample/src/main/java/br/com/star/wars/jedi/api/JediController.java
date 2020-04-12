package br.com.star.wars.jedi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import br.com.star.wars.jedi.exception.JediNotFoundException;
import br.com.star.wars.jedi.model.Jedi;
import br.com.star.wars.jedi.model.JediRepository;

@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class JediController {

	@Autowired
	private JediRepository jediRepo;

	@GetMapping
	public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return jediRepo.findAllProjected(field, page, sort);
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
