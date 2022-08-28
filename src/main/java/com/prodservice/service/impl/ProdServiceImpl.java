package com.prodservice.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
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

	public class DiscountComparator implements Comparator<ProductEntity> {

		@Override
		public int compare(ProductEntity p1, ProductEntity p2) {
			double p11 = p1.getRetailPrice() - p1.getDiscountedPrice();
			p11 = p11 / p1.getRetailPrice();
			double p22 = p2.getRetailPrice() - p2.getDiscountedPrice();
			p22 = p22 / p2.getRetailPrice();
			if ((p11 * 100) == (p22 * 100))
				return 0;
			else if ((p11 * 100) < (p22 * 100))
				return 1;
			else
				return -1;
		}
	}

	@Override
	public ProductResponse getCategory(String categoryId, String sortBy, String filter, int page, int pageSize) {
		ProductResponse productResponse = new ProductResponse();
		if (categoryId != null && sortBy != null && page != 0 && pageSize != 0) {
			Pageable pageable = null;
			if (sortBy.equals("popularity") || sortBy.equals("betterDiscount")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
			} else if (sortBy.equals("ltoh")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("discountedPrice").ascending());
			} else if (sortBy.equals("htol")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("discountedPrice").descending());
			}
			Page<ProductEntity> productList = null;
			JSONObject obj = new JSONObject(filter);
			if (obj.length() != 0) {
				Map<String, Object> filterMap = new HashMap<>();
				filterMap.putAll(obj.toMap());
				String filterStr = "";
				String priceStr = "";
				if (filterMap.get("brand") != null) {
					filterStr = filterMap.get("brand").toString();
				}
				if (filterMap.get("price") != null) {
					priceStr = filterMap.get("price").toString();
				}
				String[] filters = filterStr.split(",");
				String[] price = priceStr.split(",");
				if (filters[0].length() > 0 && price[0].length() > 0) {
					productList = prodRepository.findProductByCategoryFilterPrice(categoryId, filters,
							Integer.valueOf(price[0]), Integer.valueOf(price[1]), pageable);
				} else if (price[0].length() > 0) {
					productList = prodRepository.findProductByCategoryPrice(categoryId, Integer.valueOf(price[0]),
							Integer.valueOf(price[1]), pageable);
				} else {
					productList = prodRepository.findProductByCategoryFilter(categoryId, filters, pageable);
				}
			} else {
				productList = prodRepository.findProductByCategory(categoryId, pageable);
			}
			List<String> brands = prodRepository.findProductByCategoryBrand(categoryId);
			List<String> newList = brands.stream().distinct().collect(Collectors.toList());
			Pagination pagination = new Pagination();
			pagination.setPage(productList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(productList.getPageable().getPageSize());

			List<ProductDTO> products = new ArrayList<>();

			if (sortBy.equals("betterDiscount")) {
				ProductEntity[] array = new ProductEntity[productList.getContent().size()];
				productList.getContent().toArray(array);
				Arrays.sort(array, new DiscountComparator());
				List<ProductEntity> prodList = Arrays.asList(array);
				for (ProductEntity prod : prodList) {
					ProductDTO prods = new ProductDTO();
					BeanUtils.copyProperties(prod, prods);
					products.add(prods);
				}
			} else {
				for (ProductEntity prod : productList.getContent()) {
					ProductDTO prods = new ProductDTO();
					BeanUtils.copyProperties(prod, prods);
					products.add(prods);
				}
			}
			productResponse.setProducts(products);
			productResponse.setPagination(pagination);
			productResponse.setTotal(productList.getTotalElements());
			productResponse.setBrands(newList);
		}
		return productResponse;
	}

	@Override
	public ProductDTO getProduct(String productId) {
		ProductDTO productResponse = new ProductDTO();
		if (productId != null) {
			ProductEntity prodResponse = prodRepository.findProductByProductName(productId);
			BeanUtils.copyProperties(prodResponse, productResponse);
		}
		return productResponse;
	}

	@Override
	public ProductResponse searchProducts(String searchId, String sortBy, String filter, int page, int pageSize) {
		ProductResponse productResponse = new ProductResponse();
		if (searchId != null && sortBy != null && filter != null && page != 0 && pageSize != 0) {
			Pageable pageable = null;
			if (sortBy.equals("popularity") || sortBy.equals("betterDiscount")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
			} else if (sortBy.equals("ltoh")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("discountedPrice").ascending());
			} else if (sortBy.equals("htol")) {
				pageable = PageRequest.of(page - 1, pageSize, Sort.by("discountedPrice").descending());
			}
			Page<ProductEntity> productList = null;
			JSONObject obj = new JSONObject(filter);
			if (obj.length() != 0) {
				Map<String, Object> filterMap = new HashMap<>();
				filterMap.putAll(obj.toMap());
				String filterStr = "";
				String priceStr = "";
				if (filterMap.get("brand") != null) {
					filterStr = filterMap.get("brand").toString();
				}
				if (filterMap.get("price") != null) {
					priceStr = filterMap.get("price").toString();
				}
				String[] filters = filterStr.split(",");
				String[] price = priceStr.split(",");
				if (filters[0].length() > 0 && price[0].length() > 0) {
					productList = prodRepository.findProductBySearchFilterPrice(searchId, filters,
							Integer.valueOf(price[0]), Integer.valueOf(price[1]), pageable);
				} else if (price[0].length() > 0) {
					productList = prodRepository.findProductBySearchPrice(searchId, Integer.valueOf(price[0]),
							Integer.valueOf(price[1]), pageable);
				} else if (filters[0].length() > 0) {
					productList = prodRepository.findProductBySearchFilter(searchId, filters, pageable);
				}
				else {
					productList = prodRepository.findProductBySearchId(searchId, pageable);
				}
			} else {
				productList = prodRepository.findProductBySearchId(searchId, pageable);
			}
			List<String> brands = prodRepository.findBrandsBySearch(searchId);
			List<String> newList = brands.stream().distinct().collect(Collectors.toList());
			Pagination pagination = new Pagination();
			pagination.setPage(productList.getPageable().getPageNumber() + 1);
			pagination.setPageSize(productList.getPageable().getPageSize());

			List<ProductDTO> products = new ArrayList<>();

			if (sortBy.equals("betterDiscount")) {
				ProductEntity[] array = new ProductEntity[productList.getContent().size()];
				productList.getContent().toArray(array);
				Arrays.sort(array, new DiscountComparator());
				List<ProductEntity> prodList = Arrays.asList(array);
				for (ProductEntity prod : prodList) {
					ProductDTO prods = new ProductDTO();
					BeanUtils.copyProperties(prod, prods);
					products.add(prods);
				}
			} else {
				for (ProductEntity prod : productList.getContent()) {
					ProductDTO prods = new ProductDTO();
					BeanUtils.copyProperties(prod, prods);
					products.add(prods);
				}
			}
			productResponse.setProducts(products);
			productResponse.setPagination(pagination);
			productResponse.setTotal(productList.getTotalElements());
			productResponse.setBrands(newList);
		}
		return productResponse;
	}
}
