package com.tjf.sample.github.storage.google.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.storage.StorageService;

@RestController
@RequestMapping(path = "/file")
public class FileController {

	private StorageService storageService;
	
	public FileController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/setup")
	String setup(@RequestParam("tenant") String tenant) {
		try {
			storageService.createBucket(tenant);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return "Bucket " + tenant + " created!";
	}
	
	@GetMapping("/save")
	String save(@RequestParam("tenant") String tenant, @RequestParam("name") String name) throws Exception {
		Path path = new File(getClass().getClassLoader().getResource("lipsum.pdf").toURI()).toPath();
	    String mimeType = Files.probeContentType(path);
		byte[] data = Files.readAllBytes(path);
		storageService.saveObject(tenant, name, mimeType, data);
		
		return "File " + name + " of bucket " + tenant + " saved!";
	}


	@GetMapping("/load")
	ResponseEntity<Resource> load(@RequestParam("tenant") String tenant, @RequestParam("name") String name) throws Exception {
		byte[] data = storageService.loadObject(tenant, name);
		
		ByteArrayResource resource = new ByteArrayResource(data);
	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .contentLength(resource.contentLength())
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    ContentDisposition.attachment()
	                        .filename("whatever")
	                        .build().toString())
	            .body(resource);
	}
	
	@GetMapping("/url")
	String url() throws Exception {
		return storageService.createUploadSignedUrl("sample-storage", "teste.zip", "application/zip", "10 MINUTES", 10000000L);
	}
}