package com.prodservice.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingsStarResponse {
	private Integer oneStar = 0;
	private Integer twoStar = 0;
	private Integer threeStar = 0;
	private Integer fourStar = 0;
	private Integer fiveStar = 0;

}
