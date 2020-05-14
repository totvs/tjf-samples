package com.tjf.sample.github.securityweb.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.securityweb.component.MachineManager;

@RestController
@RequestMapping(path = "/api/v1/machine", produces = { APPLICATION_JSON_VALUE })
public class MachineController {

	@Autowired
	private MachineManager machineManager;

	@GetMapping
	public List<Map<String, Object>> machineList() {
		return machineManager.getMachines();
	}

	@PostMapping("stop")
	@RolesAllowed("SUPERVISOR")
	public List<Map<String, Object>> stopAll() {
		machineManager.stopAll();
		return machineManager.getMachines();
	}

	@PostMapping("{id}/run")
	@PreAuthorize("hasPermission(\"SampleSecurityWeb.machine.run\")")
	public Map<String, Object> run(@PathVariable int id) {
		machineManager.run(id);
		return machineManager.getMachine(id);
	}

}