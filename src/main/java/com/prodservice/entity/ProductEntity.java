package com.prodservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "products")
public class ProductEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;

	@Column(nullable = false, length = 254)
	private String productUrl;
	
	@Column(nullable = false, length = 512)
	private String productName;

	@Column(nullable = false, length = 64)
	private String productCategory;

	@Column(nullable = false)
	private Integer retailPrice;

	@Column(nullable = false)
	private Integer discountedPrice;

	@Column(nullable = false)
	private String prodImage;

	@Column(nullable = false)
	private String prodDescription;

	@Column(nullable = false, length = 50)
	private String prodBrand;
}
