package com.tjf.sample.github.messaging.amqp;

import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.totvs.tjf.messaging.context.AmqpTOTVSMessage;
import com.totvs.tjf.messaging.context.CloudEventsInfo;
import com.totvs.tjf.messaging.context.TOTVSHeader;
import com.totvs.tjf.messaging.context.TransactionInfo;
import com.totvs.tjf.mock.test.Semaphore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class StarShipControllerTest {

	@Container
	static final RabbitMQContainer container = new RabbitMQContainer("rabbitmq:3.7.7-management")
			.withExposedPorts(5672, 15672)
			.withPluginsEnabled("rabbitmq_management")
			.withUser("guest", "guest", ImmutableSet.of("administrator"))
			.withPermission("/", "guest", ".*", ".*", ".*")
			.withExchange("starship-exchange-1", "direct")
			.withQueue("starship-queue-1")
			.withBinding("starship-exchange-1", "starship-queue-1", Collections.emptyMap(), "starship-routing-key-1", "queue")
			.withExchange("starship-exchange-2", "direct")
			.withQueue("starship-queue-2")
			.withBinding("starship-exchange-2", "starship-queue-2", Collections.emptyMap(), "starship-routing-key-2", "queue")
			.withExchange("starship-exchange-3", "direct")
			.withQueue("starship-queue-3")
			.withBinding("starship-exchange-3", "starship-queue-3", Collections.emptyMap(), "starship-routing-key-3", "queue");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Semaphore semaphore;

	protected static StarShipArrivedEvent starShipArrivedEvent;
	protected static AmqpTOTVSMessage<StarShipArrivedEvent> amqpTOTVSMessage;

	@DynamicPropertySource
	static void configureContainer(DynamicPropertyRegistry registry) {
		registry.add("tjf.messaging.amqp.hostname", container::getHost);
		registry.add("tjf.messaging.amqp.port", container::getAmqpPort);
		registry.add("tjf.messaging.amqp.username", container::getAdminUsername);
		registry.add("tjf.messaging.amqp.password", container::getAdminPassword);
	}

	@Test
//	@Timeout(value = 60)
	void starShipArrivedTest() throws Exception {
		mockMvc.perform(get("/starship/arrived?name=teste"))
				.andExpect(status().isOk());
		semaphore.waitForSignal();
		assertEquals("teste", starShipArrivedEvent.getName());
	}

	@Test
//	@Timeout(value = 60)
	void starShipArrivedTmTest() throws Exception {
		mockMvc.perform(get("/starship/arrived-tm?name=teste"))
				.andExpect(status().isOk());
		semaphore.waitForSignal();
		TOTVSHeader header = amqpTOTVSMessage.getHeader();
		StarShipArrivedEvent starShipArrivedEvent1 = amqpTOTVSMessage.getContent();
		assertEquals("starShipArrivedEvent", header.getType());
		assertEquals("teste", starShipArrivedEvent1.getName());
	}

	@Test
//	@Timeout(value = 60)
	void starShipArrivedTmCeTest() throws Exception {
		mockMvc.perform(get("/starship/arrived-tm-ce?name=teste"))
				.andExpect(status().isOk());
		semaphore.waitForSignal();
		TOTVSHeader header = amqpTOTVSMessage.getHeader();
		TransactionInfo transactionInfo = header.getTransactionInfo();
		CloudEventsInfo cloudEventsInfo = header.getCloudEventsInfo();
		StarShipArrivedEvent starShipArrivedEvent1 = amqpTOTVSMessage.getContent();
		assertEquals("starShipArrivedEvent", header.getType());
		assertEquals("teste", starShipArrivedEvent1.getName());
		assertEquals("tjf-messaging-amqp-publisher", transactionInfo.getGeneratedBy());
		assertEquals("teste", cloudEventsInfo.getSubject());
		assertNotNull(cloudEventsInfo.getCorrelationId());
		assertEquals("teste", starShipArrivedEvent1.getName());
	}

}
