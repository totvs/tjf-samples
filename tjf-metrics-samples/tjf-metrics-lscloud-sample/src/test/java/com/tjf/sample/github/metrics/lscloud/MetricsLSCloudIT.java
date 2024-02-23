package com.tjf.sample.github.metrics.lscloud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.metrics.lscloud.error.ErrorSubscriber;
import com.totvs.tjf.lscloud.message.LogLicenseMessage;

@SpringBootTest(classes = LscloudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetricsLSCloudIT {

	@Autowired
	ErrorSubscriber errorSubscriber;

	@Value("${tjf.lscloud.module-id}")
	String moduleId;

	@Value("${tjf.lscloud.group-id}")
	String groupId;

	@Value("${tjf.lscloud.slot-id}")
	String slotId;

	@Autowired
	ObjectMapper mapper;

	@LocalServerPort
	private int randomServerPort;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	@Timeout(5) // Evitar loop infinito no while
	public void testAccessWithTokenAndRole() throws Exception {
		var tenantId = "987654321";
		var routine = "starShipArrived";

		var response = restTemplate.getForEntity("http://localhost:" + randomServerPort + "/starship/arrived?tenant=" + tenantId + "&name=teste2",
				String.class);
		assertEquals(200, response.getStatusCodeValue());

		// Esperar mensagen chegar
		Optional<LogLicenseMessage> logs = Optional.empty();
		while (logs.isEmpty()) {
			logs = errorSubscriber.getLog();
		}

		var log = logs.get().getLogs().get(0);

		assertEquals(tenantId, log.getTenantId());
		assertEquals(moduleId, log.getModuleId());
		assertEquals(groupId, log.getGroupId());
		assertEquals(slotId, log.getSlotId());
		assertEquals(routine, log.getRoutine());
	}
}