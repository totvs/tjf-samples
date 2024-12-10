package com.tjf.sample.github.multidb.interceptor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.totvs.tjf.core.security.context.SecurityPrincipal;

public class TenantAuthentication {

	public static void setAuthenticationInfo(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal("92e8a7dc-61d8-4045-9d80-222c774ad791", "admin", tenant,
				tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
