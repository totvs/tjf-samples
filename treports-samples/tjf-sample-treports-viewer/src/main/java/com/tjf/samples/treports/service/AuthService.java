package com.tjf.samples.treports.service;

import java.util.List;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private OAuth2AccessToken token;

	public OAuth2AccessToken getToken() {

		if (token == null || token.isExpired()) {

			ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

			resource.setAccessTokenUri("http://empresa1.localhost:5009/totvs.rac/connect/token");
			resource.setClientId("treports_ro");
			resource.setClientSecret("totvs@123");
			resource.setScope(List.of("authorization_api"));
			resource.setUsername("admin");
			resource.setPassword("totvs@123");

			AccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
			OAuth2RestOperations restTemplateA = new OAuth2RestTemplate(resource,
					new DefaultOAuth2ClientContext(accessTokenRequest));
			token = restTemplateA.getAccessToken();
			
			System.out.println("Token: " + token);
			System.out.println("#### NEW TOKEN #####");
			
			return token;
		} else {
			
			System.out.println("Token: " + token);
			System.out.println("#### GET TOKEN #####");

			return token;
		}
	}
}
