package com.prodservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "carts")
public class CartEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false, length = 64)
	private String userId;
	
	@Column(nullable = false, length = 512)
	private String productName;

	@Column(nullable = false)
	private Integer retailPrice;

	@Column(nullable = false)
	private Integer discountedPrice;

	@Column(nullable = false)
	private String productImage;

	@Column(nullable = false, length = 50)
	private String productBrand;
	
	@Column(nullable = false)
	private String productQuantity;
}
