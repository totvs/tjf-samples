package com.tjf.sample.github.apicore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ApiCoreExceptionApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApiCoreExceptionIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate template;

	@Test
	public void createStarshipWithoutExcpetionTest() throws Exception {
		String expectedResult = "{\"starship\":\"created\"}";

		mockMvc.perform(post("/api/v1/starship/create").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Millenium Falcon\",\"description\":\"Nave do Han\",\"crew\":5}"))
				.andExpect(status().isCreated()).andExpect(content().json(expectedResult));
	}

	@Test
	public void createStarshipWithExceptionTest() throws JSONException, URISyntaxException {
		String expectedResult = "{\"code\":\"StarshipCreateConstraintException\",\"message\":\"It's a trap\",\"detailedMessage\":\"The force is not with you\",\"details\":[{\"code\":\"Starship.description.Size\",\"message\":\"Ship description must not be less than 1 or greater than 15\",\"detailedMessage\":\"description: A sucata mais veloz da galaxia\"}]}";
		URI uri = new URI("/api/v1/starship/create");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag("en-US")));

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"name\":\"Millenium Falcon\",\"description\":\"A sucata mais veloz da galaxia\",\"crew\":5}",
				headers);
		ResponseEntity<String> result = template.postForEntity(uri, entity, String.class);

		assertEquals(expectedResult, result.getBody());
	}

}
