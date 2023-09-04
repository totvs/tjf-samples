package com.tjf.sample.github.securityweb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class SecurityWebIT {

	@Autowired
	private MockMvc mockMvc;

	private static String accessApplicationToken;
	private static final String URL_CONNECT = "https://tenant-c56bab97-8ea6-4b6c-8568-1cde9c6a9477.rac.dev.totvs.app/totvs.rac/connect/token";

	@Value("${tjf.security.clientid}")
	private String CLIENT_ID;

	@Value("${tjf.security.clientsecret}")
	private String CLIENT_SECRET;

	@Value("${tjf.security.login}")
	private String LOGIN;

	@Value("${tjf.security.password}")
	private String PASSWORD;

	@Value("${tjf.security.login-superv}")
	private String LOGIN_SUPERV;

	@Value("${tjf.security.password-superv}")
	private String PASSWORD_SUPERV;

	public void generateToken() throws ClientProtocolException {
		GenerateToken racAuthorization = new GenerateToken();
		accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, LOGIN, PASSWORD);
	}

	public void generateTokenSuperv() throws ClientProtocolException {
		GenerateToken racAuthorization = new GenerateToken();
		accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, LOGIN_SUPERV,
				PASSWORD_SUPERV);
	}

	@Test
	@Order(1)
	public void testAccessWithTokenAndRole() throws Exception {
		generateTokenSuperv();
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	public void testValidRole() throws Exception {
		generateTokenSuperv();
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void testAcessWithoutToken() throws Exception {
		mockMvc.perform(get("/api/v1/machine")).andExpect(status().isUnauthorized());
	}


	@Test
	@Order(4)
	public void testUnauthorizedAccess() throws Exception {
		// TJF-1737 - Comportamento precisa ser avaliado
		// mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isUnauthorized());

		generateToken();
		mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isForbidden());
	}

	@Test
	@Order(5)
	public void testAccessWithTokenNoRole() throws Exception {
		generateToken();
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}
	

	@Test
	@Order(6)
	public void testInvalidRole() throws Exception {
		generateToken();
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isForbidden());
	}

}
