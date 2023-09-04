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
	
	
	/*	RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "client_credentials");

		headers.add(AUTHORIZATION,
				getBasicAuthenticationHeader(application.getClientId(), application.getClientSecret()));
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add(RESPONSE_TYPE, RESPONSE_TYPE_TOKEN);
		headers.add(CLIENT_ID, application.getClientId());
		headers.add(CLIENT_SECRET, application.getClientSecret());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		var endpoint = fluigUrl + "/accounts/oauth/token";

		log.debug("Get token from {}", endpoint);
		return restTemplate.exchange(endpoint, HttpMethod.POST, request, FluigTokenResponse.class).getBody();
*/
	
	public String generateToken(String url, String clientId, String clientSecret, String username, String password) throws ClientProtocolException {
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
