package com.prodservice.model.response;

import java.util.List;

import com.prodservice.shared.dto.CartDTO;
import com.prodservice.shared.dto.GiftDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartResponse {
	private List<CartDTO> items;
	private Integer totalBeforeDiscount = 0;
	private Integer totalAfterDiscount = 0;
	private Integer totalDiscount = 0;
	private List<GiftDTO> gifts;
}
