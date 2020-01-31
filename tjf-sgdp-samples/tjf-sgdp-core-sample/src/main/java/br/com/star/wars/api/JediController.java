package br.com.star.wars.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.v1.request.ApiFieldRequest;
import com.totvs.tjf.api.context.v1.request.ApiPageRequest;
import com.totvs.tjf.api.context.v1.request.ApiSortRequest;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import br.com.star.wars.model.Jedi;
import br.com.star.wars.model.JediRepository;

@RestController
@RequestMapping(path = "/sgpd/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
public class JediController {

	@Autowired
	private JediRepository jediRepository;

	@GetMapping
	public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return jediRepository.findAllProjected(field, page, sort);
	}

}
