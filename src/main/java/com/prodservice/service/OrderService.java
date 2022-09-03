package com.prodservice.service;

import com.prodservice.model.response.OrdersResponse;
import com.prodservice.shared.dto.OrderItemResponse;

public interface OrderService {

	OrdersResponse getOrders(String userId, int page, int pageSize) throws Exception;

	OrderItemResponse getOrderById(String orderId, String userId) throws Exception;

}
