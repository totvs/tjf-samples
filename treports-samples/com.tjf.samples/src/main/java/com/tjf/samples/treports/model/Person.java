package com.tjf.samples.treports.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

	String name;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}
}
