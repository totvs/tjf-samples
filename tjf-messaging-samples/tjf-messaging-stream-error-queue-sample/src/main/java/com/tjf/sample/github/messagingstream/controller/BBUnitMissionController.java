package com.tjf.sample.github.messagingstream.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.messagingstream.event.BBUnitSendMission;
import com.tjf.sample.github.messagingstream.infrastructure.messaging.BBUnitPublisher;
import com.tjf.sample.github.messagingstream.model.BBUnit;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = BBUnitMissionController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class BBUnitMissionController {

	public static final String PATH = "mission";

	@Autowired
	private BBUnitPublisher bbPublisher;

	@PostMapping(path = "/send")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ApiCollectionResponse<BBUnit> sendMessageInBBUnit(@RequestBody BBUnit bbUnit) {
		BBUnitSendMission missionEvent = new BBUnitSendMission(bbUnit);
		bbPublisher.publish(missionEvent.getBbUnit(), BBUnitSendMission.MISSION);
		return ApiCollectionResponse.from(List.of(bbUnit));
	}

}