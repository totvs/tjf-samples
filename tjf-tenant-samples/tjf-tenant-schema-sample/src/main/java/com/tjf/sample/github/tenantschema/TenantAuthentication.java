package com.tjf.sample.github.tenantschema;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.totvs.tjf.core.common.security.SecurityPrincipal;

public class TenantAuthentication {

	public static void setAuthenticationInfo(String tenant) {
		// A classe SecurityPrincipal recebe três parâmetros:
		// 1 - Id do usuário, exemplo: 92e8a7dc-61d8-4045-9d80-222c774ad791
		// 2 - Código do usuário, exemplo: admin
		// 3 - Código do tenant, exemplo: 92e8a7dc-61d8-4045-9d80-222c774ad790
		// 4 - Código do tenant que será salvo no banco de dados, exemplo: 92e8a7dc
		SecurityPrincipal principal = new SecurityPrincipal("92e8a7dc-61d8-4045-9d80-222c774ad791", "admin", tenant,
				tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
