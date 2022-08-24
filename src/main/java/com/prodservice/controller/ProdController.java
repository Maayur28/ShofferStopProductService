package com.prodservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prodservice.model.response.ProductResponse;
import com.prodservice.service.ProdService;

@RestController
@RequestMapping("product")
@CrossOrigin(origins = { "http://localhost:3000", "https://www.shofferstop.com" })
public class ProdController {

	@Autowired
	ProdService prodService;

	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCategory(HttpServletRequest request, @PathVariable String categoryId,
			@RequestParam int page, @RequestParam int pageSize) throws Exception {
		try {
			ProductResponse prodResponse = prodService.getCategory(categoryId, page, pageSize);
			return ResponseEntity.ok(prodResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
}
