package com.prodservice.service;

import com.prodservice.model.response.OrdersResponse;

public interface OrderService {

	OrdersResponse getOrders(String userId, int page, int pageSize) throws Exception;

}
