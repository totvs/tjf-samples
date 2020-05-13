package com.tjf.sample.github.corevalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

	private final String timestamp;
	private final Integer status;
	private final String error;
	private final String message;
	private final String path;

}
