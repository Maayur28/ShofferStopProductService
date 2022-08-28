package com.prodservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodservice.entity.RatingEntity;
import com.prodservice.model.request.RatingCreateRequest;
import com.prodservice.model.response.RatingCreateResponse;
import com.prodservice.model.response.RatingsStarResponse;
import com.prodservice.repository.RatingRepository;
import com.prodservice.service.RatingService;
import com.prodservice.utils.ErrorMessages;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	RatingRepository ratingRepository;

	@Override
	public RatingCreateResponse createRating(RatingCreateRequest ratingCreateRequest) throws Exception {
		if (ratingCreateRequest.getUserId() == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}

		RatingEntity ratingEntity = new RatingEntity();
		ratingEntity.setUserId(ratingCreateRequest.getUserId());
		ratingEntity.setProductName(ratingCreateRequest.getProductName());
		ratingEntity.setRating(ratingCreateRequest.getRating());

		RatingEntity userRating = ratingRepository.findRatingByProductNameAndUserId(ratingCreateRequest.getUserId(),
				ratingCreateRequest.getProductName());

		if (userRating == null) {
			ratingRepository.save(ratingEntity);
		} else {
			ratingRepository.updateRatingByProductNameAndUserId(ratingCreateRequest.getUserId(),
					ratingCreateRequest.getProductName(), ratingCreateRequest.getRating());
		}
		List<RatingEntity> ratingEnt = ratingRepository.findRatingByProductName(ratingCreateRequest.getProductName());
		List<Integer> ratings = new ArrayList<>();
		for (RatingEntity rating : ratingEnt) {
			ratings.add(rating.getRating());
		}
		RatingCreateResponse ratingCreateResponse = new RatingCreateResponse();
		ratingCreateResponse.setProduct_name(ratingCreateRequest.getProductName());
		ratingCreateResponse.setUserRating(ratingCreateRequest.getRating());
		ratingCreateResponse.setTotal(ratings.size());

		int total = 0, one = 0, two = 0, three = 0, four = 0, five = 0;
		for (Integer i : ratings) {
			total += i;
			if (i == 1)
				one++;
			else if (i == 2)
				two++;
			else if (i == 3)
				three++;
			else if (i == 4)
				four++;
			else if (i == 5)
				five++;
		}

		if (ratings != null) {
			if (ratings.size() > 0) {
				double totalRating = (double) total / ratings.size();
				ratingCreateResponse.setAverageRating(Math.round(totalRating * 10) / 10.0);
			} else {
				ratingCreateResponse.setAverageRating(0);
			}
			RatingsStarResponse ratingsStarResponse = new RatingsStarResponse();
			ratingsStarResponse.setOneStar(one);
			ratingsStarResponse.setTwoStar(two);
			ratingsStarResponse.setThreeStar(three);
			ratingsStarResponse.setFourStar(four);
			ratingsStarResponse.setFiveStar(five);
			ratingCreateResponse.setRatings(ratingsStarResponse);
		}

		return ratingCreateResponse;
	}

	@Override
	public RatingCreateResponse getRating(String userId, String productName) throws Exception {
		if (productName == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.INVALID_REQUEST.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}

		RatingCreateResponse ratingCreateResponse = new RatingCreateResponse();
		ratingCreateResponse.setProduct_name(productName);

		List<Integer> ratings = new ArrayList<>();

		if (userId == null) {
			ratingCreateResponse.setUserRating(0);
		} else {
			RatingEntity userRating = ratingRepository.findRatingByProductNameAndUserId(userId, productName);
			if (userRating == null) {
				ratingCreateResponse.setUserRating(0);
			} else {
				ratingCreateResponse.setUserRating(userRating.getRating());
			}
		}

		List<RatingEntity> ratingEnt = ratingRepository.findRatingByProductName(productName);

		for (RatingEntity rating : ratingEnt) {
			ratings.add(rating.getRating());
		}
		ratingCreateResponse.setTotal(ratings.size());

		int total = 0, one = 0, two = 0, three = 0, four = 0, five = 0;
		for (Integer i : ratings) {
			total += i;
			if (i == 1)
				one++;
			else if (i == 2)
				two++;
			else if (i == 3)
				three++;
			else if (i == 4)
				four++;
			else if (i == 5)
				five++;
		}

		if (ratings != null) {
			if (ratings.size() > 0) {
				double totalRating = (double) total / ratings.size();
				ratingCreateResponse.setAverageRating(Math.round(totalRating * 10) / 10.0);
			} else {
				ratingCreateResponse.setAverageRating(0);
			}
			RatingsStarResponse ratingsStarResponse = new RatingsStarResponse();
			ratingsStarResponse.setOneStar(one);
			ratingsStarResponse.setTwoStar(two);
			ratingsStarResponse.setThreeStar(three);
			ratingsStarResponse.setFourStar(four);
			ratingsStarResponse.setFiveStar(five);
			ratingCreateResponse.setRatings(ratingsStarResponse);
		}

		return ratingCreateResponse;
	}

}
