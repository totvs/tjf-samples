package com.tjf.sample.github.tenantschema;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TenantInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
		String tenant = req.getHeader("X-Planet");
		TenantAuthentication.setAuthenticationInfo(tenant);
		return true;
	}
}
