package com.tjf.sample.github.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.messaging.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.StarShipPublisher;
import com.tjf.sample.github.persistence.Droid;
import com.tjf.sample.github.persistence.DroidRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = DroidController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class DroidController {

	public static final String PATH = "api/v1/droid";

	@Autowired
	private DroidRepository droidRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	private StarShipPublisher samplePublisher;

	public DroidController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}

	@GetMapping(path = "/getAll")
	public ApiCollectionResponse<Droid> getAllDroids() {
		List<Droid> items = new ArrayList<Droid>();
		droidRepository.findAll().forEach(items::add);
		return ApiCollectionResponse.of(items);
	}

	@GetMapping(path = "/namedQuery/getByName/{name}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByName(@PathVariable("name") String name) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByName(name);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Name", totalTime, droids);
	}

	@GetMapping(path = "/namedQuery/getByDescription/{description}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByDescription(@PathVariable("description") String description) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByDescription(description);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Description", totalTime, droids);
	}

	@GetMapping(path = "/namedQuery/getByNameAndDescription/{name}/{description}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseData namedQueryGetByNameAndDescription(@PathVariable("name") String name,
			@PathVariable("description") String description) {
		long startTime = System.nanoTime();
		List<Droid> droids = droidRepository.getByNameAndDescription(name, description);
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;

		return new ResponseData("Named Query By Name And Description", totalTime, droids);
	}

	@GetMapping(path = "/arrived/{name}")
	public String getEcho(@PathVariable("name") String name) {
		StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);
		samplePublisher.publish(starShipEvent, StarShipArrivedEvent.NAME);
		return new Date().toString();
	}

}