package com.tjf.sample.github.apijpa.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ApiJpaFilterApplication.class)
@AutoConfigureMockMvc
public class ApiJpaSimpleFiltersSampleIT {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void getSimpleFilterTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"employeeId\":1,\"name\":\"John\"}]}";
		mockMvc.perform(get("/api/v1/employees?name=John").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}

}
