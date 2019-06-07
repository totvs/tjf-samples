package com.tjf.sample.github.aggregate.infra;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Habitant {
	
	private String id;
	private String name;
	private String gender;
	
}
