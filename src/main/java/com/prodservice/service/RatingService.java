package com.prodservice.service;

import java.util.Optional;

import com.prodservice.model.request.RatingCreateRequest;
import com.prodservice.model.response.RatingCreateResponse;

public interface RatingService {

	RatingCreateResponse createRating(RatingCreateRequest ratingCreateRequest) throws Exception;

	RatingCreateResponse getRating(Optional<String> userId, String productName) throws Exception;
}
