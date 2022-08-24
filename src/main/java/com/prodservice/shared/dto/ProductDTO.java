package com.prodservice.shared.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = -6416119022329580080L;
	
	private String id;
	private String productName;
	private String productCategory;
	private Integer retailPrice;
	private Integer discountedPrice;
	private String prodImage;
	private String prodDescription;
	private String prodBrand;
}
