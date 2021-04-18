package com.tjf.sample.github.tenantschema.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.tenantschema.dto.HabitantDto;
import com.tjf.sample.github.tenantschema.model.HabitantModel;
import com.tjf.sample.github.tenantschema.model.HabitantModelRepository;

@RestController
@RequestMapping(path = "/api/v1/habitants", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitantController {

	@Autowired
	private HabitantModelRepository repository;

	@PostMapping
	public List<HabitantModel> saveAll(@RequestBody List<HabitantDto> dtos) {
		List<HabitantModel> habitants = new ArrayList<>();

		for (HabitantDto dto : dtos) {
			// Efetua a conversão do objeto recebido para o objeto de modelo.
			HabitantModel habitant = new HabitantModel();
			habitant.setId(dto.getId());
			habitant.setName(dto.getName());
			habitant.setGender(dto.getGender());
			habitants.add(habitant);
		}

		return repository.saveAll(habitants);
	}

	@GetMapping
	public List<HabitantModel> getAll() {
		return repository.findAll();
	}

}
