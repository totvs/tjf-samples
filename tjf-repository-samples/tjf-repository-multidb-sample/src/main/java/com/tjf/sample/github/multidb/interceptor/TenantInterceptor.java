package com.tjf.sample.github.multidb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

public class TenantInterceptor extends WebRequestHandlerInterceptorAdapter {

	public TenantInterceptor(WebRequestInterceptor requestInterceptor) {
		super(requestInterceptor);
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
		String tenant = req.getHeader("tenant");
		TenantAuthentication.setAuthenticationInfo(tenant);
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {

		
	}
}
