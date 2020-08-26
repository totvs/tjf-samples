package com.tjf.sample.github.messaging.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.tjf.sample.github.messaging.PublisherApplication;
import com.tjf.sample.github.messaging.test.services.StarShipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PublisherApplication.class })
@AutoConfigureMockMvc
public abstract class MessagingIT {

	public static final String ARRIVED_URL = "/starship/arrived";
	public static final String ARRIVED_WITHOUT_TENANT = "/starship/arrivedWithoutTenant";

	public static boolean error;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	StarShipService starShipService;

	public void messagingArrivedTest() throws Exception {

		sendArrivedMessaging("abc");
		expectedCounter("abc", 1);

		sendArrivedMessaging("abc");
		expectedCounter("abc", 2);

		sendArrivedMessaging("def");
		expectedCounter("def", 1);

		sendArrivedMessaging("def");
		expectedCounter("def", 2);
	}

	public void messagingWithoutTenantTest() throws Exception {
		sendArrivedWithoutTenantMessage("Millenium Falcon");
		expectedCounter(null, 1);

		sendArrivedWithoutTenantMessage("X-Wing");
		expectedCounter(null, 2);
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

	private void sendArrivedMessaging(String tenant) throws Exception {
		mockMvc.perform(getRequest(ARRIVED_URL).param("tenant", tenant)).andExpect(status().isOk());
	}

	private void sendArrivedWithoutTenantMessage(String name) throws Exception {
		mockMvc.perform(getRequest(ARRIVED_WITHOUT_TENANT).param("name", name)).andExpect(status().isOk());
	}

	private MockHttpServletRequestBuilder getRequest(String url) {
		return get(url).param("name", "nave");
	}

	@ActiveProfiles("rabbit")
	public static class MessagingForRabbitIT extends MessagingIT {

		@Test(timeout = 30_000)
		public void messagingArrivedTest() throws Exception {
			super.messagingArrivedTest();
		}

		@Test(timeout = 10000)
		public void messagingWithoutTenantTest() throws Exception {
			super.messagingWithoutTenantTest();
		}
	}

	@ActiveProfiles("kafka")
	public static class MessagingForKafkaIT extends MessagingIT {

		@Test(timeout = 30_000)
		public void messagingArrivedTest() throws Exception {
			super.messagingArrivedTest();
		}

		@Test(timeout = 10000)
		public void messagingWithoutTenantTest() throws Exception {
			super.messagingWithoutTenantTest();
		}
	}

	@ActiveProfiles("jms")
	public static class MessagingForJmsIT extends MessagingIT {

		@Test(timeout = 30_000)
		public void messagingArrivedTest() throws Exception {
			super.messagingArrivedTest();
		}

		@Test(timeout = 10000)
		public void messagingWithoutTenantTest() throws Exception {
			super.messagingWithoutTenantTest();
		}
	}
}