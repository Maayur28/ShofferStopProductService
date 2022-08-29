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
@Entity(name = "promotions")
public class PromotionEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 64)
	private String promoId;

	@Column(nullable = false, length = 512)
	private String bogo;

	@Column(nullable = false, length = 512)
	private String dotd;

	@Column(nullable = false, length = 512)
	private String at99;

	@Column(nullable = false, length = 512)
	private String at499;

	@Column(nullable = false, length = 512)
	private String at999;

	@Column(nullable = false, length = 512)
	private String promoDate;
}
