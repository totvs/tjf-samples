package com.tjf.sample.github.securityweb.requestvalidation.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestBody extends Parameter {

	public RequestBody(Parameter next) {
		super(next);
	}

	@Override
	public String get(HttpServletRequest request) {
		var value = getBody(request);
		return Objects.nonNull(value) ? value : next.get(request);
	}

	private String getBody(HttpServletRequest request) {
		try {
			var jsonBody = request.getReader().lines().collect(Collectors.joining());
			var mapper = new ObjectMapper();
			var jsonNode = mapper.readTree(jsonBody);
			return jsonNode.get("organization").asText();
		} catch (Exception e) {
			return null;
		}
	}
}
