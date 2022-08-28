package com.prodservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingCreateResponse {
	private Integer userRating = 0;
	private String product_name;
	private RatingsStarResponse ratings;
	private double averageRating = 0;
	private Integer total = 0;
}
