package com.tjf.sample.github.securityweb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TJFSecurityIT {

	@Autowired
	private MockMvc mockMvc;

	private static String accessApplicationToken;

	private static final String URL_CONNECT = "http://localhost:5009/totvs.rac/connect/token";
	
	private static final String URL_RAC = "http://localhost:5009/totvs.rac/";

	private static final String CLIENT_ID = "js_oidc_sampleapp";

	private static final String CLIENT_SECRET = "totvs@123";
	
	@BeforeClass
	public static void setup() {
		GenerateToken racAuthorization = new GenerateToken();
		
		try {
			
			String updateApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, "admin", "totvs@123");
			racAuthorization.updateRolesUser(URL_RAC, updateApplicationToken);

			accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, "admin", "totvs@123");
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testValidRole() throws Exception {
		mockMvc.perform(post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}
}
