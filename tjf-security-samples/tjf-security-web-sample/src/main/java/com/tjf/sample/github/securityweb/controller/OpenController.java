package com.tjf.sample.github.securityweb.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.securityweb.component.MachineManager;

@RestController
@RequestMapping(path = "/open/v1/machine", produces = { APPLICATION_JSON_VALUE })
public class OpenController {

	private final MachineManager machineManager;

	public OpenController(MachineManager machineManager) {
		this.machineManager = machineManager;
	}

	@GetMapping
	public List<Map<String, Object>> getMachineList() {
		return machineManager.getMachines();
	}

	@PostMapping()
	public List<Map<String, Object>> postMachineList() {
		return machineManager.getMachines();
	}

	@PutMapping()
	public List<Map<String, Object>> putMachineList() {
		return machineManager.getMachines();
	}
}
