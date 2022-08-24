package com.prodservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prodservice.entity.ProductEntity;
import com.prodservice.model.response.Pagination;
import com.prodservice.model.response.ProductResponse;
import com.prodservice.repository.ProdRepository;
import com.prodservice.service.ProdService;
import com.prodservice.shared.dto.ProductDTO;

@Service
public class ProdServiceImpl implements ProdService {

	@Autowired
	ProdRepository prodRepository;

	@Override
	public ProductResponse getCategory(String categoryId, int page, int pageSize) {
		ProductResponse productResponse = new ProductResponse();
		if (categoryId != null && page != 0 && pageSize != 0) {
			Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
			Page<ProductEntity> productList = prodRepository.findProductByCategory(categoryId, pageable);

			Pagination pagination = new Pagination();
			pagination.setPage(productList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(productList.getPageable().getPageSize());

			List<ProductDTO> products = new ArrayList<>();
			for (ProductEntity prod : productList.getContent()) {
				ProductDTO prods = new ProductDTO();
				BeanUtils.copyProperties(prod, prods);
				products.add(prods);
			}

			productResponse.setProducts(products);
			productResponse.setPagination(pagination);
			productResponse.setTotal(productList.getTotalElements());
		}
		return productResponse;
	}
}
