package com.tjf.sample.github.apicore.exception.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Starship {

	@NotBlank(message = "{Starship.name.NotBlank}")
	private String name;

	@NotBlank(message = "{Starship.description.NotBlank}")
	@Size(min = 1, max = 15, message = "{Starship.description.Size}")
	private String description;

	@Max(value = 5, message = "{Startshp.crew.Max}")
	private int crew;

}
