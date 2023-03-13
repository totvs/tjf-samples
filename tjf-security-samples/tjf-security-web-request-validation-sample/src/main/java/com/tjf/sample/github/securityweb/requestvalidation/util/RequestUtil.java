package com.tjf.sample.github.securityweb.requestvalidation.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static String getParameterValue(HttpServletRequest request) {
		Parameter parameter = new RequestParam(new RequestHeader(new RequestBody(new RequestDefault())));
		return parameter.get(request);
	}
}
