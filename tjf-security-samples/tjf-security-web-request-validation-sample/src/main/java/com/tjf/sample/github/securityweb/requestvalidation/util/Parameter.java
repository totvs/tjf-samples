package com.tjf.sample.github.securityweb.requestvalidation.util;

import javax.servlet.http.HttpServletRequest;

public abstract class Parameter {

	protected Parameter next;

	public Parameter(Parameter next) {
		this.next = next;
	}

	public abstract String get(HttpServletRequest request);
}
