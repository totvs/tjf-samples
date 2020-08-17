package com.tjf.sample.github.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.messaging.SGDPPublisher;
import com.totvs.sgdp.sdk.config.SGDPMetadata;
import com.totvs.sgdp.sdk.services.data.SGDPDataCommand;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskCommand;
import com.totvs.tjf.core.common.security.SecurityPrincipal;

@RestController
@RequestMapping(path = "/sgdp/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class SgdpController {

	@Autowired
	private SGDPPublisher publisher;

	private SGDPMetadata metadata;

	public SgdpController() {
		Resource resource = new ClassPathResource("jedi.metadata.json");
		ObjectMapper mapper = new ObjectMapper();
		try {
			File metadataFile = resource.getFile();
			this.metadata = (SGDPMetadata) mapper.readValue(metadataFile, SGDPMetadata.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("metadata")
	public String metadata(@RequestParam() StringBuffer json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.metadata = (SGDPMetadata) mapper.readValue(json.toString(), SGDPMetadata.class);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return "Metadata was set !";
	}

	@GetMapping("mask")
	public String mask(@RequestParam() String identification) {
		setTenant("10");
		SGDPMaskCommand maskCommand = new SGDPMaskCommand();
		maskCommand.setIdentifiers(new HashMap<String, String>());
		maskCommand.getIdentifiers().put("identification", identification);
		publisher.publish(maskCommand);
		return "Mask Command was sent !";
	}

	@GetMapping("data")
	public String data(@RequestParam() String identification) {
		setTenant("10");
		SGDPDataCommand dataCommand = new SGDPDataCommand();
		dataCommand.setIdentifiers(new HashMap<String, String>());
		dataCommand.getIdentifiers().put("identification", identification);
		publisher.publish(dataCommand);
		return "Data Command was sent !";
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant.split("-")[0]);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A",
				null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}