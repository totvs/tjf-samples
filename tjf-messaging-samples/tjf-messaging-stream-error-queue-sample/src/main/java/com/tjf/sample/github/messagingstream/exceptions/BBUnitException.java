package com.tjf.sample.github.messagingstream.exceptions;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.totvs.tjf.api.context.stereotype.ApiError;

@ApiError("BBUnitException")
public class BBUnitException extends ConstraintViolationException {

	private static final long serialVersionUID = -3867016605481005147L;

	public BBUnitException(Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(constraintViolations);
	}

}
