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
	private ProductDTO at99;
	private ProductDTO at499;
	private ProductDTO at999;
	public String date; 
}
