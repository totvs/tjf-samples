package com.tjf.sample.github.securityweb.requestvalidation.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class TokenUtil {

	public static String generate(String url, String clientId, String clientSecret, String username, String password) throws ClientProtocolException {
	String token = "";
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			StringEntity text = new StringEntity("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=password&username=" + username + "&password=" + password + "&scope=authorization_api");
			
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


}
