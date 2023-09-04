package com.tjf.sample.github.securityweb.requestvalidation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.tjf.sample.github.securityweb.requestvalidation.util.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestValidationIT {

	@Autowired
	private MockMvc mockMvc;
	private static final String URL_CONNECT = "https://tenant-c56bab97-8ea6-4b6c-8568-1cde9c6a9477.rac.dev.totvs.app/totvs.rac/connect/token";

	@Value("${tjf.security.clientid}")
	private String CLIENT_ID;

	@Value("${tjf.security.clientsecret}")
	private String CLIENT_SECRET;

	@Value("${tjf.security.login-superv}")
	private String LOGIN_SUPERV;

	@Value("${tjf.security.password-superv}")
	private String PASSWORD_SUPERV;
	
	@Test
	public void testParamAccessValid() throws Exception {
		mockMvc.perform(get("/api/validation/param")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT, CLIENT_ID , CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV)))
			.andExpect(status().isOk())
			.andExpect(content().string("Param"));
	}

	@Test
	public void testHeaderAccessValid() throws Exception {
		mockMvc.perform(get("/api/validation/header")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT,CLIENT_ID,CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV)))
			.andExpect(status().isOk())
			.andExpect(content().string("Header"));
	}

	@Test
	public void testBodyAccessValid() throws Exception {
		mockMvc.perform(post("/api/validation/body")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT,CLIENT_ID,CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV)))
			.andExpect(status().isOk())
			.andExpect(content().string("Body"));
	}

	@Test
	public void testParamAccessDeniedOrganization() throws Exception {
		mockMvc.perform(get("/api/validation/param")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT,CLIENT_ID,CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV))
				.param("organization", "1"))
			.andExpect(status().isForbidden());
	}

	@Test
	public void testHeaderAccessDeniedOrganization() throws Exception {
		mockMvc.perform(get("/api/validation/header")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT,CLIENT_ID,CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV))
				.header("organization", "1"))
			.andExpect(status().isForbidden());
	}

	@Test
	public void testBodyAccessDeniedOrganization() throws Exception {
		mockMvc.perform(post("/api/validation/body")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.generate(URL_CONNECT,CLIENT_ID,CLIENT_SECRET,LOGIN_SUPERV,PASSWORD_SUPERV))
				.content("{ \"organization\": \"1\" }"))
			.andExpect(status().isForbidden());
	}
}
