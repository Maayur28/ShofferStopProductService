package com.prodservice.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {
	private String productName;
	private Integer retailPrice;
	private Integer discountedPrice;
	private String productImage;
	private String productBrand;
	private Integer quantity;
}
