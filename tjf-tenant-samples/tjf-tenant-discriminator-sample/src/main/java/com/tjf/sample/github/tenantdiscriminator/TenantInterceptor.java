package com.tjf.sample.github.tenantdiscriminator;

import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class TenantInterceptor extends WebRequestHandlerInterceptorAdapter {

	public TenantInterceptor(WebRequestInterceptor requestInterceptor) {
		super(requestInterceptor);
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
		String tenant = req.getHeader("X-Planet");
		TenantAuthentication.setAuthenticationInfo(tenant);
		return true;
	}
}
