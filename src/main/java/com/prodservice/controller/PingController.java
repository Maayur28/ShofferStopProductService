package com.prodservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "https://shofferstop.vercel.app", "https://www.shofferstop.in" })
public class PingController {

	@GetMapping
	public String getPing() {
		return "Ping Successful";
	}
}
