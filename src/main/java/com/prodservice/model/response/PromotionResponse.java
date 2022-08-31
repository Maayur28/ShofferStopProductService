package com.prodservice.model.response;

import java.util.List;

import com.prodservice.shared.dto.ProductDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PromotionResponse {
	private List<ProductDTO> carouselPromo;
	private List<ProductDTO> at99;
	private List<ProductDTO> at499;
	private List<ProductDTO> at999;
	public String date;
}
