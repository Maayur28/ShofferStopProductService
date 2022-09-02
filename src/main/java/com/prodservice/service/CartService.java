package com.prodservice.service;

import java.io.IOException;

import com.prodservice.model.request.CartRequest;
import com.prodservice.model.response.CartResponse;

public interface CartService {

	CartResponse getCart(String cartId) throws IOException;

	long createCart(CartRequest cartRequest, String userId) throws Exception;

	long getCartCount(String userId) throws Exception;

}
