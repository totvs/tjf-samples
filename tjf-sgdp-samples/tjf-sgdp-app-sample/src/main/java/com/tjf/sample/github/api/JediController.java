package com.tjf.sample.github.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.model.Jedi;
import com.tjf.sample.github.model.JediRepository;

@RestController
@RequestMapping(path = "/jedi/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class JediController {

	@Autowired
	private JediRepository jediRepository;

	@GetMapping("jedis")
	public Page<Jedi> getAll(Pageable page) {
		return jediRepository.findAll(page);
	}

}