package com.prodservice.service;

import java.io.IOException;

import com.prodservice.model.response.PromotionResponse;

public interface PromotionService {

	PromotionResponse getPromotion() throws IOException;

	void clearPromotion();

}
