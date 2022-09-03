package com.prodservice.shared.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponse {
	private long id;
	private List<OrderResponseDTO> items;
	private String date;
	private Integer totalPrice = 0;
	private List<GiftDTO> gifts;
	private String fullName;
}
