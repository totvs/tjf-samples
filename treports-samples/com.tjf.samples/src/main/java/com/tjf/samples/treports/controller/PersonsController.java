package com.tjf.samples.treports.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.samples.treports.model.Person;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse.ApiCollectionResponseBuilder;

@RestController
@RequestMapping(path = "/api/rh/v1/persons", produces = "application/json")
public class PersonsController {

	/*@GetMapping
	public  ApiCollectionResponse<Person> getEmployees() {
		
		System.out.println("TREPORT");
		
		List<Person> persons = List.of(new Person("joao"), new Person("maria")); 
		
		ApiCollectionResponse<Person> response = new ApiCollectionResponseBuilder<Person>().items(persons).build(); 
				
		return response;
	}*/
	
	@GetMapping
	public String getEmployees() {
		
		System.out.println("TREPORT");

		return "{\"total\": 2, \"hasNext\": false, \"items\": [{\"id\": \"1\", \"Name\": \"joao\"}, {\"id\": \"2\", \"Name\": \"maria\"}]}";
	}
}