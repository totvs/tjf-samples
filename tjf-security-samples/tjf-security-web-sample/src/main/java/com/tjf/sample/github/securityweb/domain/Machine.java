package com.tjf.sample.github.securityweb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Machine {

	private boolean running = false;
	private final Type type;

	public void run() {
		this.running = true;
	}

	public void stop() {
		this.running = false;
	}

}