package com.tjf.sample.github.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.tjf.sample.github.MetricsMicrometerApplication;
import com.tjf.sample.github.service.MyMetricService;

@SpringBootTest(classes = MetricsMicrometerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetricsIT {

	@LocalServerPort
	private int randomServerPort;

	@Autowired
	MyMetricService myMetricService;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	@Timeout(5) // Evitar loop infinito no while
	public void testAccessWithTokenAndRole() throws Exception {
		var client = "123654789";
		myMetricService.clientIncrement(client);

		var response = restTemplate.getForEntity("http://localhost:" + randomServerPort + "/_/prometheus", String.class);
		assertEquals(200, response.getStatusCodeValue());
		assertTrue(response.getBody().contains(client));
	}
}