package com.tjf.sample.github.repositoryaggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class FamilyTreeIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFamilyTree() throws Exception {
		String padme = UUID.randomUUID().toString();

		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"" + padme + "\",\"name\":\"Padme Amidala\",\"gender\":\"female\"}}"))
				.andExpect(status().isOk());

		String leia = UUID.randomUUID().toString();

		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"" + leia + "\",\"name\":\"Leia Organa\",\"gender\":\"female\"}}"))
				.andExpect(status().isOk());

		String luke = UUID.randomUUID().toString();

		mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"" + luke + "\",\"name\":\"Luke Skywalker\",\"gender\":\"male\"}}"))
				.andExpect(status().isOk());

		// Luke filho da Padme
		mockMvc.perform(post("/api/v1/familytree/person/" + padme + "/relative/" + luke + "/child")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// Leia filha da Padme
		mockMvc.perform(post("/api/v1/familytree/person/" + padme + "/relative/" + leia + "/child")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// Padme arvore genealogica
		MvcResult result = mockMvc
				.perform(get("/api/v1/familytree/person/" + padme).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode familyTree = mapper.readTree(result.getResponse().getContentAsString());
		assertEquals(2, familyTree.get("relatives").size());
	}

}
