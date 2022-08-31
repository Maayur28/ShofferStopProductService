package com.prodservice.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prodservice.entity.ProductEntity;
import com.prodservice.model.response.PromotionResponse;
import com.prodservice.repository.ProdRepository;
import com.prodservice.repository.PromotionRepository;
import com.prodservice.service.PromotionService;
import com.prodservice.shared.dto.ProductDTO;

@Service
@CacheConfig(cacheNames = { "promotion" })
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	PromotionRepository promoRepository;

	@Autowired
	ProdRepository prodRepository;

	@CacheEvict(value = "promotion", allEntries = true)
	public void clearPromotion() {
		System.out.println("clearing cache");
	}

	@Override
	@Cacheable(cacheNames = "promotion", unless = "#result.getDate()==null")
	public PromotionResponse getPromotion() throws IOException {
		PromotionResponse promoResponse = new PromotionResponse();
		String today = LocalDate.now().toString();
		promoResponse = constructPromotion(promoResponse, today);
		if (promoResponse.getCarouselPromo() != null && promoResponse.getAt99() != null
				&& promoResponse.getAt499() != null && promoResponse.getAt499() != null) {
			promoResponse.setDate(today);
		}
		return promoResponse;
	}

	@CachePut(value = "promotion")
	public PromotionResponse constructPromotion(PromotionResponse promoResponse, String today) throws IOException {
		Pageable pageable = PageRequest.of(0, 10);
		String product99 = "", product499 = "", product999 = "", productBogo = "mayurag", productDotd = "";

		List<ProductDTO> carousel = new ArrayList<>();
		List<String> productNames = new ArrayList<>();
		List<ProductDTO> atProd = new ArrayList<>();

		// BOGO SUPPORT

		ProductDTO prodCopy = new ProductDTO();
		pageable = PageRequest.of(0, 1);
		Page<ProductEntity> product = prodRepository.findProdPromo(productBogo, pageable);
		productBogo = product.getContent().get(0).getProductName();
		BeanUtils.copyProperties(product.getContent().get(0), prodCopy);
		carousel.add(prodCopy);

		// DEAL OF THE DAY SUPPORT

		prodCopy = new ProductDTO();
		product = prodRepository.findProdPromo(productBogo, pageable);
		productDotd = product.getContent().get(0).getProductName();
		product.getContent().get(0).setDiscountedPrice(product.getContent().get(0).getRetailPrice() / 10);
		BeanUtils.copyProperties(product.getContent().get(0), prodCopy);
		carousel.add(prodCopy);

		// AT 99 Offer

		pageable = PageRequest.of(0, 10);
		product = prodRepository.findPromoAt(99, 499, pageable);
		for (ProductEntity prod : product.getContent()) {
			prodCopy = new ProductDTO();
			productNames.add(prod.getProductName());
			prod.setDiscountedPrice(99);
			BeanUtils.copyProperties(prod, prodCopy);
			atProd.add(prodCopy);
		}
		product99 = String.join(",", productNames);
		promoResponse.setAt99(atProd);
		atProd = new ArrayList<>();
		productNames = new ArrayList<>();

		// AT 499 Offer

		product = prodRepository.findPromoAt(499, 999, pageable);
		for (ProductEntity prod : product.getContent()) {
			prodCopy = new ProductDTO();
			productNames.add(prod.getProductName());
			prod.setDiscountedPrice(499);
			BeanUtils.copyProperties(prod, prodCopy);
			atProd.add(prodCopy);
		}
		product499 = String.join(",", productNames);
		promoResponse.setAt499(atProd);
		atProd = new ArrayList<>();
		productNames = new ArrayList<>();

		// AT 999 Offer

		product = prodRepository.findPromoAt(999, 9999, pageable);
		for (ProductEntity prod : product.getContent()) {
			prodCopy = new ProductDTO();
			productNames.add(prod.getProductName());
			prod.setDiscountedPrice(499);
			BeanUtils.copyProperties(prod, prodCopy);
			atProd.add(prodCopy);
		}
		product999 = String.join(",", productNames);
		promoResponse.setAt999(atProd);
		atProd = new ArrayList<>();
		productNames = new ArrayList<>();

		promoRepository.updatePromo("promo_mayur28", product99, product499, product999, productBogo, productDotd,
				today);
		promoResponse.setCarouselPromo(carousel);
		return promoResponse;
	}

	/*
	 * private void constuctImage(PromotionResponse promoResponse,
	 * Page<ProductEntity> product, List<ProductDTO> prods, List<String>
	 * productName, int count) throws IOException { int curr = 0; for (ProductEntity
	 * prod : product.getContent()) { boolean isValid = false; String[] images =
	 * prod.getProdImage().split(","); for (String image : images) { image =
	 * image.replace("[", "").replace("]", ""); image = image.replace("\"",
	 * "").replace("\"", ""); image = image.replace("http://img5a",
	 * "https://rukminim1").replace("http://img6a", "https://rukminim1") .trim();
	 * isValid = validateUrl(image); if (isValid) { curr++; ProductDTO prodCopy =
	 * new ProductDTO(); productName.add(prod.getProductName());
	 * prod.setProdImage(image); BeanUtils.copyProperties(prod, prodCopy);
	 * prods.add(prodCopy); break; } } if (count == curr) break; } }
	 * 
	 * private boolean validateUrl(String prodUrl) throws IOException { URL url =
	 * new URL(prodUrl); HttpURLConnection huc = (HttpURLConnection)
	 * url.openConnection(); huc.setInstanceFollowRedirects(false); int responseCode
	 * = huc.getResponseCode(); if (responseCode == 200) { return true; } else {
	 * return false; } }
	 */

}
