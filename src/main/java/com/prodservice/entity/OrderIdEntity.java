package com.prodservice.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "orderitems")
public class OrderIdEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

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

	@Column(nullable = true)
	private String promotionMessage;
}
