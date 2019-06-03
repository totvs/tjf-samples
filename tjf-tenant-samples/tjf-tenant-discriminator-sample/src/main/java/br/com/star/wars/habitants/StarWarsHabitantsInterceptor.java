package br.com.star.wars.habitants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class StarWarsHabitantsInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
		String tenant = req.getHeader("X-Planet");
		StarWarsHabitantsAuthentication.setAuthenticationInfo(tenant);
		return true;
	}
}
