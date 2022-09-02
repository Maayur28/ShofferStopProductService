package com.prodservice.shared.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GiftDTO implements Serializable {

	private static final long serialVersionUID = -6416119022329580080L;

	private String productName;
	private String productImage;
	private String productBrand;
	private String productQuantity;
}
