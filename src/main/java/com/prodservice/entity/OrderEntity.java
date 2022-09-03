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
@Entity(name = "orders")
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 64)
	private String userId;

	@Column(nullable = false)
	private String addressId;

	@Column(nullable = false)
	private String orderIds;

	@Column(nullable = false)
	private String giftIds;

	@Column(nullable = false)
	private Integer totalBeforeDiscount = 0;

	@Column(nullable = false)
	private Integer totalAfterDiscount = 0;

	@Column(nullable = false)
	private Integer totalDiscount = 0;

	@Column(nullable = false)
	private String date;

	@Column(nullable = true)
	private String orderDates;

}
