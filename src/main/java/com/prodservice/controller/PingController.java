package com.prodservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "https://www.shofferstop.com" })
public class PingController {

	@GetMapping
	public String getPing() {
		return "Ping Successful";
	}
}
