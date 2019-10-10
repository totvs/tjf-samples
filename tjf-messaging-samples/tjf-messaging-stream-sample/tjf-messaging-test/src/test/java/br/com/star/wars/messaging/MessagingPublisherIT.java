package br.com.star.wars.messaging;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.star.wars.messaging.PublisherApplication;
import br.com.star.wars.messaging.services.StarShipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PublisherApplication.class })
@AutoConfigureMockMvc
public class MessagingPublisherIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	StarShipService starShipService;

	@Test(timeout = 10000)
	public void messagingTest() throws Exception {

		sendMessaging("abc");
		expectedCounter("abc", 1);

		sendMessaging("abc");
		expectedCounter("abc", 2);

		sendMessaging("def");
		expectedCounter("def", 1);

		sendMessaging("def");
		expectedCounter("def", 2);
	}

	private void expectedCounter(String tenant, int amount) throws InterruptedException {

		HashMap<String, Integer> counter = new HashMap<>();

		// Aguarda mensagem ser processada
		while (counter.get(tenant) == null || counter.get(tenant) < amount) {			
			counter = starShipService.getCounter();
			if (counter.get(tenant) == null || counter.get(tenant) < amount)
				Thread.sleep(100);
		}
	}

	private void sendMessaging(String tenant) throws Exception {

		mockMvc.perform(get("/starship/arrived")
				.param("name", "nave")
				.param("tenant", tenant))
				.andExpect(status().isOk());

	}
}