package com.nk.blog.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class ApiController {

	@Value ("${artifact.name}")
	private String artifact;
	
	@Value ("${spring.profiles.active}")
	private String environment;

	


	/**
	 * Status api
	 * @param locale
	 * @return String
	 */
	@GetMapping("/status")
	public ResponseEntity<String> status(Locale locale) {
		log.info("API status.......:{}", locale);
		return ResponseEntity
                .ok()
                .body(this.artifact + " is UP. Environment: " + this.environment.toUpperCase());
	}
}