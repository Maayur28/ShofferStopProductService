package com.prodservice.model.response;

import java.util.List;

import com.prodservice.shared.dto.ProductDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
	private List<ProductDTO> products;
	private Pagination pagination;
	private long total;
	private List<String> brands;
}
