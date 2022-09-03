package com.prodservice.model.request;

import com.prodservice.model.response.CartResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
	private UserCreateAddressRequestDTO address;
	private CartResponse cart;
}
