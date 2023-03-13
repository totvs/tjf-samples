package com.tjf.sample.github.securityweb.requestvalidation.config;

import com.tjf.sample.github.securityweb.requestvalidation.util.RequestUtil;
import com.totvs.tjf.security.requestvalidation.ValidationResult;
import com.totvs.tjf.security.requestvalidation.Validator;
import com.totvs.tjf.security.requestvalidation.organization.OrganizationsAllowed;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class OrganizationValidator implements Validator {

	@Override
	public ValidationResult validate(HttpServletRequest request) {

		String organization = RequestUtil.getParameterValue(request);
		if (Objects.isNull(organization)) {
			return ValidationResult.valid();
		}

		if (Arrays.asList(OrganizationsAllowed.value()).contains(organization)) {
			return ValidationResult.valid();
		}

		return ValidationResult.invalid("Acesso negado para a organização");
	}
}
