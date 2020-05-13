package com.tjf.sample.github.messagingstream;

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

@SpringBootTest(classes = MessagingStreamApplication.class)
@AutoConfigureMockMvc
public class MessagingStreamErrorQueueIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testsendMessage() throws Exception {
		String expectedResult = "{\"messages\":1}";

		mockMvc.perform(post("/mission/send").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"BB-8\",\"mission\":\"Get and Protected Luke location\"}"))
				.andExpect(status().isCreated());

		Thread.sleep(2_000);

		mockMvc.perform(get("/actuator/messaging")).andExpect(status().isOk())
				.andExpect(content().json(expectedResult));
	}

}
