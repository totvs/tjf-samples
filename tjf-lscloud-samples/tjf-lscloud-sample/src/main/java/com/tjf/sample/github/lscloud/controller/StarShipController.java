package com.tjf.sample.github.lscloud.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.core.common.security.SecurityPrincipal;
import com.totvs.tjf.lscloud.message.LogLicense;
import com.totvs.tjf.lscloud.messaging.Lscloud;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private Lscloud lscloud;

	public StarShipController(Lscloud lscloud) {
		this.lscloud = lscloud;
	}

	@GetMapping("/arrived")
	String starShipArrived(String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		var logLicense = LogLicense.builder().withRoutine("starShipArrived").build();
		lscloud.log(logLicense);

		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent to lscloud!";
	}

	@GetMapping("/error")
	String error() {

		var logLicense = LogLicense.builder().withRoutine("").build();
		lscloud.log(logLicense);

		return "View error on log!";
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}