package br.com.star.wars.exceptions;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.totvs.tjf.api.context.stereotype.ApiError;

@ApiError("BBUnitException")
public class BBUnitException extends ConstraintViolationException {

	private static final long serialVersionUID = 1L;

	public BBUnitException(Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(constraintViolations);

	}

}
