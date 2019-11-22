package br.com.star.wars;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CoreValidationIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	TestRestTemplate template;

	@Test
	public void createStarshipWithoutExcpetionTest() throws Exception {
		String expectedResult = "{\n" + "    \"starship\": \"created\"\n" + "}";

		mockMvc.perform(post("/api/v1/starship/create").contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + "    \"name\": \"Millenium Falcon\",\n" + "    \"description\": \"Nave do Han\",\n"
						+ "    \"crew\": 5\n" + "}"))
				.andExpect(status().isCreated()).andExpect(content().json(expectedResult));
	}

	@Test
	public void createStarshipWithExceptionTest() throws JSONException, URISyntaxException {

		HttpHeaders headers = new HttpHeaders();

		String expectedResult = "{\"code\":\"StarshipCreateConstraintException\",\"message\":\"It's a trap\",\"detailedMessage\":\"The force is not with you\",\"details\":[{\"code\":\"Starship.description.Size\",\"message\":\"{Starship.description.Size}\",\"detailedMessage\":\"description: A sucata mais veloz da galáxia\"}]}";
		final String baseUrl = "/api/v1/starship/create";
		URI uri = new URI(baseUrl);

		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\n" + "    \"name\": \"Millenium Falcon\",\n"
						+ "    \"description\": \"A sucata mais veloz da galáxia\",\n" + "    \"crew\": 5\n" + "}",
				headers);

		ResponseEntity<String> result = template.postForEntity(uri, entity, String.class);

		assertTrue(result.getBody().equals(expectedResult));
	}
}
