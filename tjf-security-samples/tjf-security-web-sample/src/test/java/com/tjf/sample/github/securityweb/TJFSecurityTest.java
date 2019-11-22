package com.tjf.sample.github.securityweb;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.totvs.tjf.mock.test.RacEmulator;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TJFSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	private static RacEmulator racEmulator;
	private static String wrongToken;

	@BeforeClass
	public static void start() {
		racEmulator = RacEmulator.getInstance();
		racEmulator.generateRSAKeys();
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		mockMvc.perform(post("/api/v1/machine/stop")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testValidRole() throws Exception {
		String jwt = racEmulator.generateJWT("super", "SUPERVISOR");
		mockMvc.perform(post("/api/v1/machine/stop").header(RacEmulator.HEADER_STRING, jwt)).andExpect(status().isOk());
	}

	@Test
	public void testForbiddenRole() throws Exception {
		String jwt = racEmulator.generateJWT("john", "USER");
		mockMvc.perform(post("/api/v1/machine/stop").header(RacEmulator.HEADER_STRING, jwt))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testValidProductRole() throws Exception {
		String jwt = racEmulator.generateJWT("super", "", new String[] { "SUPERVISOR" });
		mockMvc.perform(post("/api/v1/machine/stop").header(RacEmulator.HEADER_STRING, jwt)).andExpect(status().isOk());
	}

	@Test
	public void testForbiddenProductRole() throws Exception {
		String jwt = racEmulator.generateJWT("john", "", new String[] { "USER" });
		mockMvc.perform(post("/api/v1/machine/stop").header(RacEmulator.HEADER_STRING, jwt))
				.andExpect(status().isForbidden());
	}
}
