package br.com.star.wars.habitants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.tjf.sample.github.tenantschema.TenantApplication;

@SpringBootTest(classes = TenantApplication.class)
@AutoConfigureMockMvc
public class SchemaHabitantsIT {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void tatooineHabitantsTest() throws Exception {
		String expectedResult = "[{\"id\":\"anakin\",\"name\":\"Anakin Skywalker\",\"gender\":\"male\"},{\"id\":\"luke\",\"name\":\"Luke Skywalker\",\"gender\":\"male\"},{\"id\":\"han\",\"name\":\"Han Solo\",\"gender\":\"male\"}]";

		mockMvc.perform(post("/api/v1/habitants").header("X-Planet", "tatooine").contentType(MediaType.APPLICATION_JSON)
				.content(
						"[{\"id\":\"anakin\",\"name\":\"Anakin Skywalker\",\"gender\":\"male\"},{\"id\":\"luke\",\"name\":\"Luke Skywalker\",\"gender\":\"male\"},{\"id\":\"han\",\"name\": \"Han Solo\",\"gender\":\"male\"}]"))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));

		mockMvc.perform(get("/api/v1/habitants").header("X-Planet", "tatooine").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void alderaanHabitantsTest() throws Exception {
		String expectedResult = "[{\"id\":\"leia\",\"name\":\"Leia Organa\",\"gender\":\"female\"}]";

		mockMvc.perform(post("/api/v1/habitants").header("X-Planet", "alderaan").contentType(MediaType.APPLICATION_JSON)
				.content("[{\"id\":\"leia\",\"name\":\"Leia Organa\",\"gender\":\"female\"}]"))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));

		mockMvc.perform(get("/api/v1/habitants").header("X-Planet", "alderaan").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void bespinHabitantsTest() throws Exception {
		String expectedResult = "[{\"id\":\"lando\",\"name\":\"Lando Calrissian\",\"gender\":\"male\"},{\"id\":\"dengar\",\"name\":\"Dengar, The Bounty Hunter\",\"gender\":\"male\"}]";

		mockMvc.perform(
				post("/api/v1/habitants").header("X-Planet", "bespin").contentType(MediaType.APPLICATION_JSON).content(
						"[{\"id\":\"lando\",\"name\":\"Lando Calrissian\",\"gender\":\"male\"},{\"id\":\"dengar\",\"name\":\"Dengar, The Bounty Hunter\",\"gender\": \"male\"}]"))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));

		mockMvc.perform(get("/api/v1/habitants").header("X-Planet", "bespin").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

}
