package br.com.star.wars;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ErrorQueueIT {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void sendMessageTest() throws Exception {

		String expectedResult = "{\n" + "    \"messages\": 1\n" + "}";

		mockMvc.perform(post("/mission/send").contentType(MediaType.APPLICATION_JSON).content(
				"{\n" + "    \"name\": \"BB-8\",\n" + "    \"mission\": \"Get and Protected Luke location\"\n" + "}"))
				.andExpect(status().isCreated());

		Thread.sleep(5000);

		mockMvc.perform(get("/actuator/messaging")).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));

	}

}
