package com.tjf.sample.github.securityweb.domain;

public class Machine {		
	
	private boolean running = false;	
	private final Type type; 

	public Type getType() {
		return type;
	}
    		
	public Machine(Type type) {
		this.type = type;
	}		
	
	public boolean running() {
		return running;
	}
	
	public void run() {
		this.running = true;
	}
	
	public void stop() {
		this.running = false;
	}
}