package com.tjf.sample.github.persistence;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Droid {

	@Id
	private int id;
	private String name;
	private String description;
	private int droidClass;
}
