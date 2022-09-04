package com.prodservice.service.impl;

import java.io.IOException;
import java.time.LocalDate;
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

import com.prodservice.entity.CartEntity;
import com.prodservice.entity.ProductEntity;
import com.prodservice.entity.PromotionEntity;
import com.prodservice.model.request.WishlistRequest;
import com.prodservice.model.response.Pagination;
import com.prodservice.model.response.ProductResponse;
import com.prodservice.repository.CartRepository;
import com.prodservice.repository.ProdRepository;
import com.prodservice.repository.PromotionRepository;
import com.prodservice.service.ProdService;
import com.prodservice.service.PromotionService;
import com.prodservice.shared.dto.ProductDTO;

@Service
public class ProdServiceImpl implements ProdService {

	@Autowired
	ProdRepository prodRepository;

	@Autowired
	PromotionService promoService;

	@Autowired
	PromotionRepository promoRepository;

	@Autowired
	CartRepository cartRepository;

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
	public ProductResponse getCategory(String categoryId, String sortBy, String filter, int page, int pageSize)
			throws IOException {
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
			PromotionEntity promoResponse = promoRepository.findByPromoId("promo_mayur28");
			if (promoResponse == null || !promoResponse.getPromoDate().equals(LocalDate.now().toString())) {
				promoService.clearPromotion();
				promoService.getPromotion();
				promoResponse = promoRepository.findByPromoId("promo_mayur28");
			}
			for (ProductDTO prods : products) {
				setProductPromo(prods, promoResponse);
			}
			productResponse.setProducts(products);
			productResponse.setPagination(pagination);
			productResponse.setTotal(productList.getTotalElements());
			productResponse.setBrands(newList);

		}
		return productResponse;
	}

	@Override
	public ProductResponse getWishlist(WishlistRequest wishlistRequest) throws IOException {
		ProductResponse productResponse = new ProductResponse();
		if (wishlistRequest != null && wishlistRequest.getProducts() != null
				&& !wishlistRequest.getProducts().isEmpty()) {
			List<ProductEntity> productList = prodRepository
					.findProductByProductNameList(wishlistRequest.getProducts());

			List<ProductDTO> products = new ArrayList<>();

			for (ProductEntity prod : productList) {
				ProductDTO prods = new ProductDTO();
				BeanUtils.copyProperties(prod, prods);
				products.add(prods);
			}
			PromotionEntity promoResponse = promoRepository.findByPromoId("promo_mayur28");
			if (promoResponse == null || !promoResponse.getPromoDate().equals(LocalDate.now().toString())) {
				promoService.clearPromotion();
				promoService.getPromotion();
				promoResponse = promoRepository.findByPromoId("promo_mayur28");
			}
			for (ProductDTO prods : products) {
				setProductPromo(prods, promoResponse);
			}
			productResponse.setProducts(products);
		}
		return productResponse;
	}

	@Override
	public ProductDTO getProduct(String productId, String userId) throws IOException {
		ProductDTO productResponse = new ProductDTO();
		if (productId != null) {
			ProductEntity prodResponse = prodRepository.findProductByProductName(productId);
			PromotionEntity promoResponse = promoRepository.findByPromoId("promo_mayur28");
			if (promoResponse == null || !promoResponse.getPromoDate().equals(LocalDate.now().toString())) {
				promoService.clearPromotion();
				promoService.getPromotion();
				promoResponse = promoRepository.findByPromoId("promo_mayur28");
			}
			BeanUtils.copyProperties(prodResponse, productResponse);
			setProductPromo(productResponse, promoResponse);
			if (userId != null) {
				CartEntity cartRes = cartRepository.findCartByProductNameAndUserId(userId, productId);
				if (cartRes != null) {
					productResponse.setPresentInBag(true);
				}
			}
		}
		return productResponse;
	}

	private void setProductPromo(ProductDTO productResponse, PromotionEntity promoResponse) {
		List<String> at99 = Arrays.asList(promoResponse.getAt99().split(","));
		List<String> at499 = Arrays.asList(promoResponse.getAt499().split(","));
		List<String> at999 = Arrays.asList(promoResponse.getAt999().split(","));
		if (productResponse.getProductName().equalsIgnoreCase(promoResponse.getBogo())) {
			productResponse.setPromotionMessage("Buy One Get One");
		} else if (productResponse.getProductName().equalsIgnoreCase(promoResponse.getDotd())) {
			productResponse.setPromotionMessage("Flash Deal");
			productResponse.setDiscountedPrice(productResponse.getRetailPrice() / 10);
		} else if (at99.contains(productResponse.getProductName())) {
			productResponse.setPromotionMessage("At ₹99 Only");
			productResponse.setDiscountedPrice(99);
		} else if (at499.contains(productResponse.getProductName())) {
			productResponse.setPromotionMessage("At ₹499 Only");
			productResponse.setDiscountedPrice(499);
		} else if (at999.contains(productResponse.getProductName())) {
			productResponse.setPromotionMessage("At ₹999 Only");
			productResponse.setDiscountedPrice(999);
		}
	}

	@Override
	public ProductResponse searchProducts(String searchId, String sortBy, String filter, int page, int pageSize)
			throws IOException {
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
				} else {
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

			PromotionEntity promoResponse = promoRepository.findByPromoId("promo_mayur28");
			if (promoResponse == null || !promoResponse.getPromoDate().equals(LocalDate.now().toString())) {
				promoService.clearPromotion();
				promoService.getPromotion();
				promoResponse = promoRepository.findByPromoId("promo_mayur28");
			}
			for (ProductDTO prods : products) {
				setProductPromo(prods, promoResponse);
			}

			productResponse.setProducts(products);
			productResponse.setPagination(pagination);
			productResponse.setTotal(productList.getTotalElements());
			productResponse.setBrands(newList);
		}
		return productResponse;
	}
}
