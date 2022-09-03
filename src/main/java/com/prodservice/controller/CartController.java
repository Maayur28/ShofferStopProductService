package com.prodservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prodservice.model.request.CartRequest;
import com.prodservice.model.request.CartUpdateRequest;
import com.prodservice.model.request.OrderRequest;
import com.prodservice.model.response.CartResponse;
import com.prodservice.service.CartService;

@RestController
@RequestMapping("cart")
@CrossOrigin(origins = { "http://localhost:3000", "https://www.shofferstop.com" })
public class CartController {

	@Autowired
	CartService cartService;

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCart(@PathVariable String userId) throws Exception {
		try {
			if (userId != null) {
				CartResponse cartResponse = cartService.getCart(userId);
				return ResponseEntity.ok(cartResponse);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> createCart(@RequestBody CartRequest cartRequest, @PathVariable String userId)
			throws Exception {
		try {
			long cartCount = cartService.createCart(cartRequest, userId);
			return ResponseEntity.ok(cartCount);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/order/{userId}", method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, @PathVariable String userId)
			throws Exception {
		try {
			String orderId = cartService.createOrder(orderRequest, userId);
			return ResponseEntity.ok(orderId);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCart(@RequestBody CartUpdateRequest cartUpdateRequest, @PathVariable String userId)
			throws Exception {
		try {
			if (userId != null) {
				CartResponse cartResponse = cartService.updateCart(cartUpdateRequest, userId);
				return ResponseEntity.ok(cartResponse);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCart(@RequestParam String productName, @PathVariable String userId)
			throws Exception {
		try {
			if (userId != null) {
				CartResponse cartResponse = cartService.deleteCart(productName, userId);
				return ResponseEntity.ok(cartResponse);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/count/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCartCount(@PathVariable String userId) throws Exception {
		try {
			if (userId != null) {
				long cartCount = cartService.getCartCount(userId);
				return ResponseEntity.ok(cartCount);
			} else {
				return new ResponseEntity<>("User doesnot exists", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
}
