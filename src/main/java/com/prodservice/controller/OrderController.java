package com.prodservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prodservice.model.response.OrdersResponse;
import com.prodservice.service.OrderService;
import com.prodservice.shared.dto.OrderItemResponse;

@RestController
@RequestMapping("order")
@CrossOrigin(origins = { "http://localhost:3000", "https://www.shofferstop.com" })
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping
	public ResponseEntity<?> getOrders(@RequestParam String userId, @RequestParam int page, @RequestParam int pageSize)
			throws Exception {
		try {
			if (userId != null) {
				OrdersResponse ordersResponse = orderService.getOrders(userId, page, pageSize);
				return ResponseEntity.ok(ordersResponse);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderById(@PathVariable String orderId, @RequestParam String userId) throws Exception {
		try {
			if (userId != null && orderId != null) {
				OrderItemResponse orderItemResponse = orderService.getOrderById(orderId, userId);
				return ResponseEntity.ok(orderItemResponse);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

}
