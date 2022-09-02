package com.prodservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartUpdateRequest {
	private String productName;
	private String productQuantity;
}
