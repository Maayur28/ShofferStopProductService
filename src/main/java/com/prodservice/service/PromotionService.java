package com.prodservice.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.prodservice.model.response.PromotionResponse;

public interface PromotionService {

	PromotionResponse getPromotion() throws IOException, IllegalAccessException, InvocationTargetException;

}
