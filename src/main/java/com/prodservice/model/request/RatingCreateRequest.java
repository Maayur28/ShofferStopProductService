package com.prodservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingCreateRequest {
	private String productName;
	private String userId;
	private Integer rating;
}
