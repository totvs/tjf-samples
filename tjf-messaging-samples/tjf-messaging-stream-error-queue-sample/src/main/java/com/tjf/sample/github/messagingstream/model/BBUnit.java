package com.tjf.sample.github.messagingstream.model;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BBUnit {

	@NotBlank
	private String name;

	private String partner;

	@NotBlank
	private String mission;

	@NotBlank
	private String message;

}
