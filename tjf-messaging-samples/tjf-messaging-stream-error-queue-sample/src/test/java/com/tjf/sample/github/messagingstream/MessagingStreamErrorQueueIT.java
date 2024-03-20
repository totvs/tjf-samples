package com.tjf.sample.github.messagingstream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = MessagingStreamApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class MessagingStreamErrorQueueIT {
	
	RestTemplate restTemplate = new RestTemplate();

	@LocalServerPort
	private int randomServerPort;
	
	//@Autowired
	//private MockMvc mockMvc;

	//@Test
	public void testsendMessage() throws Exception {
		String expectedResult = "{\"messages\":1}";

		String jsonData = "{\n" +
				"  \"name\": \"BB-8\",\n" +
				"  \"partner\": \"Poe Dameron\",\n" +
				"  \"mission\": \"Get the location of Luke Skywalker\"\n" +
				"}";

		/*mockMvc.perform(post("/mission/send")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonData))
						.andExpect(status().isCreated());*/
		
      HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + randomServerPort + "/mission/send", // URL do endpoint
                HttpMethod.POST, // Método HTTP POST
                requestEntity,
                String.class
        );

        // Verifica se o status da resposta é 201 Created
        assert responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value();

        Thread.sleep(5_000);

	/*mockMvc.perform(get("/actuator/messaging"))
				.andExpect(status().isOk())
				.andExpect(content().json(expectedResult));*/
        
        ResponseEntity<String> responseEntityActuator = restTemplate.getForEntity(
        		"http://localhost:" + randomServerPort + "/actuator/messaging", // URL do endpoint
                String.class
        );

        // Verifica se o status da resposta é 200 OK
        assert responseEntity.getStatusCodeValue() == HttpStatus.OK.value();
	}

}
