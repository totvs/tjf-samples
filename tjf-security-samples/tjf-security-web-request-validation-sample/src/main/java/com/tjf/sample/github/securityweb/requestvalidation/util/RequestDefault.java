package com.tjf.sample.github.securityweb.requestvalidation.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestDefault extends Parameter {

	public RequestDefault() {
		super(null);
	}

	@Override
	public String get(HttpServletRequest request) {
		return null;
	}
}
