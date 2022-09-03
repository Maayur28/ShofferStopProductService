package com.prodservice.model.response;

import java.util.List;

import com.prodservice.shared.dto.OrderItemResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdersResponse {
	private List<OrderItemResponse> orderItems;
	private Pagination pagination;
	private long total = 0;
}
