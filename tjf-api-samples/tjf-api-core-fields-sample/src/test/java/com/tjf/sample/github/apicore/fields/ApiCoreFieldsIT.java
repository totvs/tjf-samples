package com.tjf.sample.github.apicore.fields;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ApiCoreFieldsApplication.class)
@AutoConfigureMockMvc
public class ApiCoreFieldsIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void findJediTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Luke Skywalker\",\"gender\":\"Masculino\",\"species\":\"Humano\",\"height\":1.72},{\"name\":\"Anakin Skywalker\",\"gender\":\"Masculino\",\"species\":\"Humano\",\"height\":1.88},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"Masculino\",\"species\":\"Humano\",\"height\":1.82},{\"name\":\"Mace Windu\",\"gender\":\"Masculino\",\"species\":\"Humano\",\"height\":1.92},{\"name\":\"Yoda\",\"gender\":\"Masculino\",\"species\":\"Desconhecida\",\"height\":0.66}]}";
		mockMvc.perform(get("/api/v1/jedi/find").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

	@Test
	public void filterJediUsingNameAndGenderTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Luke Skywalker\",\"gender\":\"Masculino\"},{\"name\":\"Anakin Skywalker\",\"gender\":\"Masculino\"},{\"name\":\"Obi-Wan Kenobi\",\"gender\":\"Masculino\"},{\"name\":\"Mace Windu\",\"gender\":\"Masculino\"},{\"name\":\"Yoda\",\"gender\":\"Masculino\"}]}";
		mockMvc.perform(get("/api/v1/jedi/find?fields=name,gender").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

	@Test
	public void filterJediUsingNameTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"name\":\"Luke Skywalker\"},{\"name\":\"Anakin Skywalker\"},{\"name\":\"Obi-Wan Kenobi\"},{\"name\":\"Mace Windu\"},{\"name\":\"Yoda\"}]}";
		mockMvc.perform(get("/api/v1/jedi/find?fields=name").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

}
