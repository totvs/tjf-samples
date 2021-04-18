package com.tjf.sample.github.tenantdiscriminator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class CustomWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        
    	long startTime = System.currentTimeMillis();
    	
    	List<String> tenant = exchange.getRequest().getHeaders().get("X-Planet");
		
    	System.out.println(" tenant TODO ");
    	System.out.println(tenant.get(0));
		
    	//TenantAuthentication.setAuthenticationInfo(tenant);
		
        return chain.filter(exchange).doFinally(signalType -> {
            long totalTime = System.currentTimeMillis() - startTime;
            exchange.getAttributes().put("totalTime", totalTime);
            System.out.println(totalTime);
        });
    }
}
