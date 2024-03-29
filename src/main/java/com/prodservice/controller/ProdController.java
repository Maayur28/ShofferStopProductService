package com.prodservice.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

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

import com.prodservice.model.request.RatingCreateRequest;
import com.prodservice.model.request.WishlistRequest;
import com.prodservice.model.response.ProductResponse;
import com.prodservice.model.response.PromotionResponse;
import com.prodservice.model.response.RatingCreateResponse;
import com.prodservice.service.ProdService;
import com.prodservice.service.PromotionService;
import com.prodservice.service.RatingService;
import com.prodservice.shared.dto.ProductDTO;

@RestController
@RequestMapping("product")
@CrossOrigin(origins = { "https://shofferstop.vercel.app", "https://www.shofferstop.in" })
public class ProdController {

	@Autowired
	ProdService prodService;

	@Autowired
	RatingService ratingService;

	@Autowired
	PromotionService promoService;

	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCategory(HttpServletRequest request, @PathVariable String categoryId,
			@RequestParam String sortBy, @RequestParam String filter, @RequestParam int page,
			@RequestParam int pageSize) {
		try {
			ProductResponse prodResponse = prodService.getCategory(categoryId, sortBy, filter, page, pageSize);
			return ResponseEntity.ok(prodResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity<?> getProduct(HttpServletRequest request, @PathVariable String productId,
			@RequestParam String userId) {
		try {
			ProductDTO prodResponse = prodService.getProduct(productId, userId);
			return ResponseEntity.ok(prodResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/wishlist", method = RequestMethod.POST)
	public ResponseEntity<?> getWishlist(@RequestBody WishlistRequest wishlistRequest) {
		try {
			ProductResponse prodResponse = prodService.getWishlist(wishlistRequest);
			return ResponseEntity.ok(prodResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/ratings", method = RequestMethod.POST)
	public ResponseEntity<?> createUserRating(@RequestBody RatingCreateRequest ratingCreateRequest) {
		try {
			RatingCreateResponse totalRatings = ratingService.createRating(ratingCreateRequest);
			return ResponseEntity.ok(totalRatings);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/ratings", method = RequestMethod.GET)
	public ResponseEntity<?> getRating(@RequestParam String userId, @RequestParam String productName) {
		try {
			RatingCreateResponse totalRatings = ratingService.getRating(userId, productName);
			return ResponseEntity.ok(totalRatings);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/search/{searchId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSearchProducts(@PathVariable String searchId, @RequestParam String sortBy,
			@RequestParam String filter, @RequestParam int page, @RequestParam int pageSize) {
		try {
			ProductResponse prodResponse = prodService.searchProducts(searchId, sortBy, filter, page, pageSize);
			return ResponseEntity.ok(prodResponse);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/promotions", method = RequestMethod.GET)
	public ResponseEntity<?> getPromotions() {
		try {
			PromotionResponse promoResponse = promoService.getPromotion();
            if (promoResponse == null || !promoResponse.getDate().equals(LocalDate.now().toString())) {
                promoService.clearPromotion();
                promoResponse = promoService.getPromotion();
            }
            return ResponseEntity.ok(promoResponse);
        } catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
}
