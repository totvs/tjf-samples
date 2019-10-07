package br.com.star.wars.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.servlet.MockMvc;

import br.com.star.wars.messaging.services.StarShipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PublisherApplication.class })
@AutoConfigureMockMvc
public class MessagingIT {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	StarShipService starShipService;

	//@Test(timeout = 10000)
	@Test
	public void messagingTest() throws Exception {

		String tenant = "abc";
		
		mockMvc.perform(get("/starship/arrived")
				.param("name", "nave")
				.param("tenant", tenant))
				.andExpect(status().isOk());
		
		HashMap<String, Integer> counter = waitCounter(0, tenant, 1);
		
		assertTrue(counter.get(tenant).equals(1));

		assertNotEquals(counter.size(), 0);
		assertTrue(counter.get(tenant).equals(1));
		mockMvc.perform(get("/starship/arrived")
				.param("name", "nave")
				.param("tenant", tenant))
				.andExpect(status().isOk());
		
		counter = waitCounter(0, tenant, 2);
		
		assertTrue(counter.get(tenant).equals(2));
		
		tenant = "def";
		
		assertNotEquals(counter.size(), 0);
		assertTrue(counter.get(tenant).equals(1));
		mockMvc.perform(get("/starship/arrived")
				.param("name", "nave")
				.param("tenant", tenant))
				.andExpect(status().isOk());
		
		counter = waitCounter(1, tenant, 1);
		
		assertTrue(counter.get(tenant).equals(1));
		
		assertNotEquals(counter.size(), 0);
		assertTrue(counter.get(tenant).equals(1));
		mockMvc.perform(get("/starship/arrived")
				.param("name", "nave")
				.param("tenant", tenant))
				.andExpect(status().isOk());
		
		counter = waitCounter(1, tenant, 2);
		
		assertTrue(counter.get(tenant).equals(2));
	}
	
	private HashMap<String, Integer> waitCounter(int size, String tenant, int amount) throws InterruptedException {
		
		HashMap<String, Integer> counter = new HashMap<>();
		
		// Aguarda mgs ser processada
		while(counter.size() <= size || counter.get(tenant) < amount) {
			counter = starShipService.getCounter();
			if(counter.size() <= size || counter.get(tenant) < amount)
				Thread.sleep(100);
		}
		
		return counter;
	}
}