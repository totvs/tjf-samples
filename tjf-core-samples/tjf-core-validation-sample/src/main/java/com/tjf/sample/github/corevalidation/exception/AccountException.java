package com.tjf.sample.github.corevalidation.exception;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class AccountException extends ConstraintViolationException {

	private static final long serialVersionUID = 1L;

	public AccountException(Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(constraintViolations);
	}

}
