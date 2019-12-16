package com.tjf.sample.github.securityweb;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GenerateToken {
	
	public String generateToken(String url, String clientId, String clientSecret, String username, String password) throws ClientProtocolException {
		String token = "";
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(url);
			
			StringEntity text = new StringEntity("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=password&username=" + username + "&password=" + password + "&scope=authorization_api&acr_values=tenant:empresa1");
			
			post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			post.setHeader("charset", "UTF-8");

			post.setEntity(text);
			
			HttpResponse response = client.execute(post);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String retSrc = EntityUtils.toString(entity);
				JSONObject result = new JSONObject(retSrc);
												
				return token = result.getString("access_token");				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return token;
	}

	/*
	 * Updates the profile in rac, including the second role (2), which was imported by the plugin. 
	 */
	public void updateRolesUser(String url, String accessApplicationToken) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPut put = new HttpPut(url + "/api/user/2");
			
			StringEntity params = new StringEntity("{\r\n" + 
					"    \"id\": 2,\r\n" + 
					"    \"userName\": \"admin\",\r\n" + 
					"    \"emailAddress\": \"admin@seudominio.com.br\",\r\n" + 
					"    \"fullName\": \"Administrador do Tenant\",\r\n" + 
					"    \"name\": \"Administrador\",\r\n" + 
					"    \"surname\": \"do Tenant\",\r\n" + 
					"    \"phoneNumber\": null,\r\n" + 
					"    \"isUndefinedPassword\": false,\r\n" + 
					"    \"sentEmailSuccessfully\": true,\r\n" + 
					"    \"isActive\": true,\r\n" + 
					"    \"externalId\": null,\r\n" + 
					"    \"organizations\": [],\r\n" + 
					"    \"roles\": [\r\n" + 
					"        2,\r\n" + 
					"        4\r\n" + 
					"    ],\r\n" + 
					"    \"productRoles\": [],\r\n" + 
					"    \"_expandables\": []\r\n" + 
					"}");
				
			put.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken);
			put.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			put.setHeader("charset", "UTF-8");
			put.setEntity(params);

			client.execute(put);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
