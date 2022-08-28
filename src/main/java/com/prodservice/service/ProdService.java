package com.prodservice.service;

import com.prodservice.model.response.ProductResponse;
import com.prodservice.shared.dto.ProductDTO;

public interface ProdService {

	ProductResponse getCategory(String categoryId, String sortBy, String filter, int page, int pageSize);

	ProductDTO getProduct(String productId);
}