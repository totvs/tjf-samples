package com.tjf.sample.github.securityweb.requestvalidation.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.MediaType;

public class TokenUtil {

	private static final String URL_CONNECT = "http://rac:8080/totvs.rac/connect/token";
	private static final String URL_RAC = "http://rac:8080/totvs.rac/";
	private static final String CLIENT_ID = "js_oidc_sampleapp";
	private static final String CLIENT_SECRET = "totvs@123";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "totvs@123";
	private static final String SUPERVISOR_ROLE = "1";

	private static String generate() {
		String token = "";

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			StringEntity text = new StringEntity("client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&grant_type=password&username=" + USERNAME + "&password=" + PASSWORD + "&scope=authorization_api&acr_values=tenant:empresa1");

			HttpPost post = new HttpPost(URL_CONNECT);
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

	private static void updateRolesUser(String productRoles) {
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
				"    \"organizations\": [ \"1\", \"2\" ],\n" +
				"    \"roles\": [2],\n" +
				"    \"productRoles\": [" + productRoles + "],\n" +
				"    \"_expandables\": []\n" +
				"}");

			String accessApplicationToken = generate();

			HttpPut put = new HttpPut(URL_RAC + "/api/user/2");
			put.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken);
			put.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			put.setEntity(params);

			client.execute(put);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String createAccessSupervisorRole() {
		updateRolesUser(SUPERVISOR_ROLE);
		return generate();
	}
}
