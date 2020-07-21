package com.tjf.sample.github.apicontext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ApiContextApplication.class)
@AutoConfigureMockMvc
public class ApiContextIT {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() {
		Locale.setDefault(new Locale("pt"));
	}

	@Test
	public void newJedi() throws Exception {
		String content = "{\"name\":\"Rey\",\"gender\":\"female\"}";

		mockMvc.perform(post("/api/v1/jedis").content(content).contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(5)));
	}

	@Test
	public void getJedi() throws Exception {
		mockMvc.perform(get("/api/v1/jedis/3").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.name", is("Yoda")))
				.andExpect(jsonPath("$.gender", is("male")));
	}

	@Test
	public void getJediException() throws Exception {
		mockMvc.perform(get("/api/v1/jedis/99").contentType(APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is("JediNotFoundException")))
				.andExpect(jsonPath("$.message", is("Jedi n\u00e3o encontrado")))
				.andExpect(jsonPath("$.detailedMessage", is("Jedi de c\u00f3digo 99 n\u00e3o foi encontrado")));
	}

	@Test
	public void getAllJedis() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"id\":1,\"name\":\"Luke Skywalker\",\"gender\":\"male\"},{\"id\":2,\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\"},{\"id\":3,\"name\":\"Yoda\",\"gender\":\"male\"},{\"id\":4,\"name\":\"Anakin Skywalker\",\"gender\":\"male\"}]}";

		mockMvc.perform(get("/api/v1/jedis").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	public void getAllJedisPagingHasNextFalse() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"id\":3,\"name\":\"Yoda\",\"gender\":\"male\"},{\"id\":4,\"name\":\"Anakin Skywalker\",\"gender\":\"male\"}]}";

		mockMvc.perform(get("/api/v1/jedis?page=2&pageSize=2").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	public void getAllJedisPagingHasNextTrue() throws Exception {
		String expectedResult = "{\"hasNext\":true,\"items\":[{\"id\":1,\"name\":\"Luke Skywalker\",\"gender\":\"male\"},{\"id\":2,\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\"},{\"id\":3,\"name\":\"Yoda\",\"gender\":\"male\"}]}";

		mockMvc.perform(get("/api/v1/jedis?page=1&pageSize=3").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	public void getAllJedisPagingSortingAsc() throws Exception {
		String expectedResult = "{\"hasNext\":true,\"items\":[{\"id\":4,\"name\":\"Anakin Skywalker\",\"gender\":\"male\"},{\"id\":1,\"name\":\"Luke Skywalker\",\"gender\":\"male\"}]}";

		mockMvc.perform(get("/api/v1/jedis?page=1&pageSize=2&order=name").contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void getAllJedisPagingSortingDesc() throws Exception {
		String expectedResult = "{\"hasNext\":true,\"items\":[{\"id\":3,\"name\":\"Yoda\",\"gender\":\"male\"},{\"id\":2,\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\"}]}";

		mockMvc.perform(get("/api/v1/jedis?page=1&pageSize=2&order=-name").contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void getAllJedisPagingFields() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Yoda\"},{\"name\":\"Anakin Skywalker\"}]}";

		mockMvc.perform(get("/api/v1/jedis?page=2&pageSize=2&fields=name").contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

}
