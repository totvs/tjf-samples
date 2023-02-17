package com.tjf.sample.github.securityweb.requestvalidation.controller;

import com.tjf.sample.github.securityweb.requestvalidation.dto.ValidationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/validation")
public class ValidationController {

	@GetMapping("/param")
	ResponseEntity<String> param(@RequestParam(required = false) String organization) {
		return ResponseEntity.ok("Param");
	}

	@GetMapping("/header")
	ResponseEntity<String> header(@RequestHeader(required = false) String organization) {
		return ResponseEntity.ok("Header");
	}

	@PostMapping("/body")
	ResponseEntity<String> body(@RequestBody(required = false) ValidationDto organization) {
		return ResponseEntity.ok("Body");
	}
}
