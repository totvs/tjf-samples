package com.tjf.sample.github.securityweb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityWebIT {

	@Autowired
	private MockMvc mockMvc;

	private static String accessApplicationToken;
	private static final String URL_CONNECT = "http://rac:8080/totvs.rac/connect/token";
	private static final String URL_RAC = "http://rac:8080/totvs.rac/";
	private static final String CLIENT_ID = "js_oidc_sampleapp";
	private static final String CLIENT_SECRET = "totvs@123";
	private static final String SUPERVISOR_ROLE = "1";
	private static final String NO_ROLE = "0";

	private void updateRoles(String roles) {
		GenerateToken racAuthorization = new GenerateToken();

		try {
			String updateApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET,
					"admin", "totvs@123");
			racAuthorization.updateRolesUser(URL_RAC, updateApplicationToken, roles);
			accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, "admin",
					"totvs@123");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAccessWithTokenAndRole() throws Exception {
		updateRoles(SUPERVISOR_ROLE);
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testAccessWithTokenNoRole() throws Exception {
		updateRoles(NO_ROLE);
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testAcessWithoutToken() throws Exception {
		mockMvc.perform(get("/api/v1/machine")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testValidRole() throws Exception {
		updateRoles(SUPERVISOR_ROLE);
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testInvalidRole() throws Exception {
		updateRoles(NO_ROLE);
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isForbidden());
	}

}
