package com.prodservice.shared.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO implements Serializable {

	private static final long serialVersionUID = 4974596675918415089L;

	private long id;
	private String productName;
	private Integer retailPrice;
	private Integer discountedPrice;
	private String productImage;
	private String productBrand;
	private String promotionMessage;
	private String productQuantity;

}
