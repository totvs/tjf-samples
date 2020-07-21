package com.tjf.sample.github.apicontext.exception;

import org.springframework.http.HttpStatus;

import com.totvs.tjf.api.context.stereotype.ApiError;
import com.totvs.tjf.api.context.stereotype.ApiErrorParameter;

@ApiError(status = HttpStatus.NOT_FOUND, value = "JediNotFoundException")
public class JediNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -52548244535504794L;

	@ApiErrorParameter
	private final int jediId;

	public JediNotFoundException(int jediId) {
		this.jediId = jediId;
	}

	public int getJediId() {
		return jediId;
	}

}
