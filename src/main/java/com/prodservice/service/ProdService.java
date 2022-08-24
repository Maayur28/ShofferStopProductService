package com.prodservice.service;

import com.prodservice.model.response.ProductResponse;

public interface ProdService {

	ProductResponse getCategory(String categoryId, int page, int pageSize);
}