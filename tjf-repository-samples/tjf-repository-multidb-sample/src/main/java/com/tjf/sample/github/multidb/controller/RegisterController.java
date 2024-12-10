package com.tjf.sample.github.multidb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.multidb.entity.Jedi;
import com.tjf.sample.github.multidb.repository.JediRepository;
import com.totvs.tjf.core.security.context.SecurityDetails;
import com.totvs.tjf.core.security.context.SecurityPrincipal;

@RestController
@RequestMapping(path = "/api/v1/register", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterController {

	@Autowired
	private JediRepository repository;

	@PostMapping
	public Jedi saveJedi(@RequestBody Jedi dto, @RequestHeader String tenant) {	
		return repository.saveAndFlush(dto);
	}

	@GetMapping
	public List<Jedi> getAll(@RequestHeader String tenant) {
		return repository.findAll();
	}
	
}
