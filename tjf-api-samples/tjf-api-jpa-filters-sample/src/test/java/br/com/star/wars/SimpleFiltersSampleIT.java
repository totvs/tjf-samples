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

import com.totvs.tjf.api.jpa.CashAccountApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CashAccountApplication.class)
@AutoConfigureMockMvc
public class SimpleFiltersSampleIT {
	
	@Autowired
	MockMvc mockMvc;
		
	@Test
	public void getSimpleFilterTest() throws Exception {
		String expectedResult = "{\"hasNext\":false,\"items\":[{\"employeeId\":1,\"name\":\"John\"}]}";

		mockMvc.perform(get("/api/v1/employees?name=John").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResult));
	}
}
