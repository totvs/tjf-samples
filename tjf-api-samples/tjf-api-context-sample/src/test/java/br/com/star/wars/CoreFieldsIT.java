package br.com.star.wars;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarWarsServicesApplication.class)
@AutoConfigureMockMvc
public class CoreFieldsIT {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void findJediTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[1]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]},{\"name\":\"Anakin Skywalker\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]},{\"name\":\"Mace Windu\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Count Dooku\",\"gender\":\"male\",\"movies\":[2,3]},{\"name\":\"Luke Skywalker\",\"gender\":\"male\",\"movies\":[4,5,6,7,8]},{\"name\":\"Rey\",\"gender\":\"female\",\"movies\":[7,8]}]}";

		mockMvc.perform(get("/api/v1/jedis").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	public void filterAndSortJediByNameAndGenderTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Anakin Skywalker\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Count Dooku\",\"gender\":\"male\",\"movies\":[2,3]},{\"name\":\"Luke Skywalker\",\"gender\":\"male\",\"movies\":[4,5,6,7,8]},{\"name\":\"Mace Windu\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]},{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[1]},{\"name\":\"Rey\",\"gender\":\"female\",\"movies\":[7,8]},{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]}]}";

		mockMvc.perform(get("/api/v1/jedis?order=name,-gender").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void filterAndSortJediByMoviesTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Anakin Skywalker\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Count Dooku\",\"gender\":\"male\",\"movies\":[2,3]},{\"name\":\"Luke Skywalker\",\"gender\":\"male\",\"movies\":[4,5,6,7,8]},{\"name\":\"Mace Windu\",\"gender\":\"male\",\"movies\":[1,2,3]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]},{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[1]},{\"name\":\"Rey\",\"gender\":\"female\",\"movies\":[7,8]},{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[1,2,3,4,5,6]}]}";

		mockMvc.perform(get("/api/v1/jedis?order=movies").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

}
