package com.prodservice.service;

import java.io.IOException;

import com.prodservice.model.request.CartRequest;
import com.prodservice.model.request.CartUpdateRequest;
import com.prodservice.model.request.OrderRequest;
import com.prodservice.model.response.CartResponse;

public interface CartService {

	CartResponse getCart(String cartId) throws IOException;

	long createCart(CartRequest cartRequest, String userId) throws Exception;

	long getCartCount(String userId) throws Exception;

	CartResponse updateCart(CartUpdateRequest cartUpdateRequest, String userId) throws Exception;

	CartResponse deleteCart(String productName, String userId) throws Exception;

	String createOrder(OrderRequest orderRequest, String userId) throws Exception;

}
