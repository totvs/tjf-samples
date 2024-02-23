package com.tjf.sample.github.securityweb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityWebFluigIT {

	@Autowired
	private MockMvc mockMvc;

	private static String accessApplicationToken = null;
	private static final String URL_CONNECT = "https://app.fluigidentity.net/accounts/oauth/token";

	@Value("${tjf.security.clientid-fluig}")
	private String CLIENT_ID;

	@Value("${tjf.security.clientsecret-fluig}")
	private String CLIENT_SECRET;

	@Value("${tjf.security.login-fluig}")
	private String LOGIN;

	@Value("${tjf.security.password-fluig}")
	private String PASSWORD;

	@Value("${tjf.security.login-superv-fluig}")
	private String LOGIN_SUPERV;

	@Value("${tjf.security.password-superv-fluig}")
	private String PASSWORD_SUPERV;

	public void generateToken() throws ClientProtocolException {
		GenerateToken racAuthorization = new GenerateToken();
		accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, LOGIN, PASSWORD);
	}

	public void generateTokenSuperv() throws ClientProtocolException {
		GenerateToken racAuthorization = new GenerateToken();
		accessApplicationToken = racAuthorization.generateToken(URL_CONNECT, CLIENT_ID, CLIENT_SECRET, LOGIN_SUPERV, PASSWORD_SUPERV);
	}

	@Test
	@Order(1)
	@DirtiesContext
	public void testAccessWithTokenAndRole() throws Exception {
		generateTokenSuperv();
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DirtiesContext
	public void testValidRole() throws Exception {
		generateTokenSuperv();
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				// TODO .andExpect(status().isOk());
				.andExpect(status().isForbidden());
	}

	@Test
	@Order(3)
	@DirtiesContext
	public void testAcessWithoutToken() throws Exception {
		mockMvc.perform(get("/api/v1/machine")).andExpect(status().isUnauthorized());
	}


	@Test
	@Order(4)
	@DirtiesContext
	public void testUnauthorizedAccess() throws Exception {
		// TJF-1737 - Comportamento precisa ser avaliado
		// mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isUnauthorized());

		generateToken();
		mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isForbidden());
	}

	@Test
	@Order(5)
	@DirtiesContext
	public void testAccessWithTokenNoRole() throws Exception {
		
		
		generateToken();
		mockMvc.perform(get("/api/v1/machine").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isOk());
	}
	

	@Test
	@Order(6)
	@DirtiesContext
	public void testInvalidRole() throws Exception {
		generateToken();
		mockMvc.perform(
				post("/api/v1/machine/stop").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessApplicationToken))
				.andExpect(status().isForbidden());
	}

}