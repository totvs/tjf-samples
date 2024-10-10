package com.tjf.sample.github.messaging.amqp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.totvs.tjf.messaging.context.AmqpTOTVSMessage;
import com.totvs.tjf.messaging.context.CloudEventsInfo;
import com.totvs.tjf.messaging.context.TOTVSHeader;
import com.totvs.tjf.messaging.context.TransactionInfo;
import com.totvs.tjf.mock.test.Semaphore;

//@Testcontainers
@SpringBootTest(classes = AmqpPublisherApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StarShipControllerIT {

	//@Container
	//static 
	final RabbitMQContainer container = new RabbitMQContainer("rabbitmq:3.7.7-management")
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

	/*@DynamicPropertySource
	static void configureContainer(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", container::getHost);
		registry.add("spring.rabbitmq.port", container::getAmqpPort);
		registry.add("spring.rabbitmq.username", container::getAdminUsername);
		registry.add("spring.rabbitmq.password", container::getAdminPassword);
	}*/

	@Test
	@Timeout(value = 60)
	@Order(1)
	@DirtiesContext
	void starShipArrivedTest() throws Exception {
		mockMvc.perform(get("/starship/arrived?name=teste"))
				.andExpect(status().isOk());
		semaphore.waitForSignal();
		assertEquals("teste", starShipArrivedEvent.getName());
	}

	@Test
	@Timeout(value = 60)
	@DirtiesContext
	@Order(2)
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
	@Timeout(value = 60)
	@DirtiesContext
	@Order(3)
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
