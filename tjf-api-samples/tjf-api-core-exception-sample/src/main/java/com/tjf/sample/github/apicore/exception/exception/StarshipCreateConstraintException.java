package com.tjf.sample.github.apicore.exception.exception;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import com.totvs.tjf.api.context.stereotype.error.ApiBadRequest;

@ApiBadRequest("StarshipCreateConstraintException")
public class StarshipCreateConstraintException extends ConstraintViolationException {

	private static final long serialVersionUID = 1L;

	public StarshipCreateConstraintException(Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(constraintViolations);
	}

}
