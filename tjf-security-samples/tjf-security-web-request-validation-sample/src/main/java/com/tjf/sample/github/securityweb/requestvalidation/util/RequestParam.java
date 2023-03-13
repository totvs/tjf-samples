package com.tjf.sample.github.securityweb.requestvalidation.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestParam extends Parameter {

	public RequestParam(Parameter next) {
		super(next);
	}

	@Override
	public String get(HttpServletRequest request) {
		var value = request.getParameter("organization");
		return Objects.nonNull(value) ? value : next.get(request);
	}
}
