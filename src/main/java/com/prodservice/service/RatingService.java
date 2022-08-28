package com.prodservice.service;

import com.prodservice.model.request.RatingCreateRequest;
import com.prodservice.model.response.RatingCreateResponse;

public interface RatingService {

	RatingCreateResponse createRating(RatingCreateRequest ratingCreateRequest) throws Exception;

	RatingCreateResponse getRating(String userId, String productName) throws Exception;
}
