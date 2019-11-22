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
public class JpaSampleIT {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void getWithPageAndPageSizeTest() throws Exception {
		String expectedResult = "{\"hasNext\":true,\"items\":[{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"}]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]}]}";

		mockMvc.perform(get("/api/v1/jedis?pageSize=2&page=1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
	
	@Test
	public void getAllParametersTest() throws Exception {
		String expectedResult = "{\"hasNext\":true,\"items\":[{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]}]}";

		mockMvc.perform(get("/api/v1/jedis?page=1&pageSize=1&order=-name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
	
	@Test
	public void getOnlyPageTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[]}";

		mockMvc.perform(get("/api/v1/jedis?page=2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
	
	@Test
	public void getOnlyOrderAscTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Anakin Skywalker\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]},{\"name\":\"Count Dooku\",\"gender\":\"male\",\"movies\":[{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]},{\"name\":\"Luke Skywalker\",\"gender\":\"male\",\"movies\":[{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"},{\"id\":7,\"name\":\"The Force Awakens\"},{\"id\":8,\"name\":\"The Last Jedi\"}]},{\"name\":\"Mace Windu\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]},{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"}]},{\"name\":\"Rey\",\"gender\":\"female\",\"movies\":[{\"id\":7,\"name\":\"The Force Awakens\"},{\"id\":8,\"name\":\"The Last Jedi\"}]},{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]}]}";

		mockMvc.perform(get("/api/v1/jedis?order=+name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
	
	@Test
	public void getOnlyOrderDescTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Yoda\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]},{\"name\":\"Rey\",\"gender\":\"female\",\"movies\":[{\"id\":7,\"name\":\"The Force Awakens\"},{\"id\":8,\"name\":\"The Last Jedi\"}]},{\"name\":\"Qui-Gon Jinn\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"}]},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"},{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"}]},{\"name\":\"Mace Windu\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]},{\"name\":\"Luke Skywalker\",\"gender\":\"male\",\"movies\":[{\"id\":4,\"name\":\"A New Hope\"},{\"id\":5,\"name\":\"The Empire Strikes Back\"},{\"id\":6,\"name\":\"The Return of the Jedi\"},{\"id\":7,\"name\":\"The Force Awakens\"},{\"id\":8,\"name\":\"The Last Jedi\"}]},{\"name\":\"Count Dooku\",\"gender\":\"male\",\"movies\":[{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]},{\"name\":\"Anakin Skywalker\",\"gender\":\"male\",\"movies\":[{\"id\":1,\"name\":\"The Phantom Menace\"},{\"id\":2,\"name\":\"Attack of the Clones\"},{\"id\":3,\"name\":\"Revenge of the Sith\"}]}]}";

		mockMvc.perform(get("/api/v1/jedis?order=-name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
	
}
