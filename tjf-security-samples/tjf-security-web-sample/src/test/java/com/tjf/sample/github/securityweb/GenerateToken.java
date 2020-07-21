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
import org.springframework.http.MediaType;

public class GenerateToken {
	
	public String generateToken(String url, String clientId, String clientSecret, String username, String password) throws ClientProtocolException {
		String token = "";
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			StringEntity text = new StringEntity("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=password&username=" + username + "&password=" + password + "&scope=authorization_api&acr_values=tenant:empresa1");
			
			HttpPost post = new HttpPost(url);
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
	public void updateRolesUser(String url, String accessApplicationToken, String productRoles) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			StringEntity params = new StringEntity("{\n" + 
					"    \"id\": 2,\n" + 
					"    \"userName\": \"admin\",\n" + 
					"    \"emailAddress\": \"admin@seudominio.com.br\",\n" + 
					"    \"fullName\": \"Administrador do Tenant\",\n" + 
					"    \"name\": \"Administrador\",\n" + 
					"    \"surname\": \"do Tenant\",\n" + 
					"    \"phoneNumber\": null,\n" + 
					"    \"isUndefinedPassword\": false,\n" + 
					"    \"sentEmailSuccessfully\": null,\n" + 
					"    \"isActive\": true,\n" + 
					"    \"externalId\": null,\n" + 
					"    \"organizations\": [],\n" + 
					"    \"roles\": [2],\n" + 
					"    \"productRoles\": [" + productRoles + "],\n" + 
					"    \"_expandables\": []\n" + 
					"}");
			
			HttpPut put = new HttpPut(url + "/api/user/2");
			put.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken);
			put.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			put.setEntity(params);

			client.execute(put);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
