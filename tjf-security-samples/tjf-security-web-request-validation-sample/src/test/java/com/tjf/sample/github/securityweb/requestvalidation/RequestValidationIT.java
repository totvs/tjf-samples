package com.tjf.sample.github.securityweb.requestvalidation;

import com.tjf.sample.github.securityweb.requestvalidation.util.TokenUtil;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestValidationIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testParamAccessValid() throws Exception {
		mockMvc.perform(get("/api/validation/param")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole()))
			.andExpect(status().isOk())
			.andExpect(content().string("Param"));
	}

	@Test
	public void testHeaderAccessValid() throws Exception {
		mockMvc.perform(get("/api/validation/header")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole()))
			.andExpect(status().isOk())
			.andExpect(content().string("Header"));
	}

	@Test
	public void testBodyAccessValid() throws Exception {
		mockMvc.perform(post("/api/validation/body")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole()))
			.andExpect(status().isOk())
			.andExpect(content().string("Body"));
	}

	@Test
	public void testParamAccessDeniedOrganization() throws Exception {
		mockMvc.perform(get("/api/validation/param")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole())
				.param("organization", "1"))
			.andExpect(status().isForbidden());
	}

	@Test
	public void testHeaderAccessDeniedOrganization() throws Exception {
		mockMvc.perform(get("/api/validation/header")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole())
				.header("organization", "1"))
			.andExpect(status().isForbidden());
	}

	@Test
	public void testBodyAccessDeniedOrganization() throws Exception {
		mockMvc.perform(post("/api/validation/body")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + TokenUtil.createAccessSupervisorRole())
				.content(" { \"organization\": \"1\" }"))
			.andExpect(status().isForbidden());
	}
}
