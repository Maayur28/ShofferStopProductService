package com.prodservice.service;

import java.io.IOException;

import com.prodservice.model.request.WishlistRequest;
import com.prodservice.model.response.ProductResponse;
import com.prodservice.shared.dto.ProductDTO;

public interface ProdService {

	ProductResponse getCategory(String categoryId, String sortBy, String filter, int page, int pageSize)
			throws IOException;

	ProductResponse searchProducts(String searchId, String sortBy, String filter, int page, int pageSize)
			throws IOException;

	ProductDTO getProduct(String productId, String userId) throws IOException;

	ProductResponse getWishlist(WishlistRequest wishlistRequest) throws IOException;
}