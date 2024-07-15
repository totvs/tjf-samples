package com.tjf.sample.github.storage.google;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import junitx.framework.FileAssert;

@SpringBootTest(classes = StorageGoogleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileStorageGoogleIT {

	RestTemplate rest = new RestTemplate();

	@LocalServerPort
	private int randomServerPort;

	private static String TENANT = UUID.randomUUID().toString();
	private static String name = "documento.pdf";

	@Test
	@Order(1)
	void testCreateBucket() {
		var response = rest.getForEntity("http://localhost:" + randomServerPort + "/file/setup?tenant=" + TENANT, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Bucket " + TENANT + " created!", response.getBody());
	}

	@Test
	@Order(2)
	void testSendFile() {
		var response = rest.getForEntity("http://localhost:" + randomServerPort + "/file/save?tenant=" + TENANT + "&name=" + name, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("File " + name + " of bucket " + TENANT + " saved!", response.getBody());
	}

	@Test
	@Order(3)
	void testDownloadFile() throws Exception {
		var expectedFile = new File(getClass().getClassLoader().getResource("lipsum.pdf").toURI()).toPath().toFile();
		var response = rest.getForEntity("http://localhost:" + randomServerPort + "/file/load?tenant=" + TENANT + "&name=" + name, Resource.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		FileAssert.assertEquals(expectedFile, toFile(response.getBody()));
	}

	File toFile(Resource resource) throws IOException {
		File file = File.createTempFile("temp", "pdf");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(resource.getContentAsByteArray());
		fos.close();

		return file;
	}
}
