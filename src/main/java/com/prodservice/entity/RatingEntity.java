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
@Entity(name = "ratings")
public class RatingEntity implements Serializable {

	private static final long serialVersionUID = -4790658905115028616L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, length = 512)
	private String productName;

	@Column(nullable = false, length = 64)
	private String userId;

	@Column(nullable = false)
	private Integer rating;

}
