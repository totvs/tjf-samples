package com.tjf.sample.github.messagingstream.event;

import com.tjf.sample.github.messagingstream.model.BBUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BBUnitSendMission {

	public static final String MISSION = "BBUnitSendMission";
	public static final String CONDITIONAL_EXPRESSION = "headers['type']=='" + MISSION + "'";

	private BBUnit bbUnit;

}
