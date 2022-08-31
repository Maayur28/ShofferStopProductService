package com.prodservice.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
		Pageable pageable = PageRequest.of(0, 5);
		String product99 = "", product499 = "", product999 = "", productBogo = "mayurag", productDotd = "";
		String[] promoAtProd = new String[3];
		int index = 0;
		List<ProductDTO> carousel = new ArrayList<>();
		ProductDTO prods = new ProductDTO();

		while (product99.isEmpty()) {
			Page<ProductEntity> product = prodRepository.findPromoAt(99, 499, pageable);
			product99 = constuctImage(promoResponse, product, prods);
			promoAtProd[index] = product99;
			index += 1;
			prods.setDiscountedPrice(99);
			promoResponse.setAt99(prods);
		}
		while (product499.isEmpty()) {
			prods = new ProductDTO();
			Page<ProductEntity> product = prodRepository.findPromoAt(499, 999, pageable);
			product499 = constuctImage(promoResponse, product, prods);
			promoAtProd[index] = product499;
			index += 1;
			prods.setDiscountedPrice(499);
			promoResponse.setAt499(prods);
		}
		while (product999.isEmpty()) {
			prods = new ProductDTO();
			Page<ProductEntity> product = prodRepository.findPromoAt(999, 9999, pageable);
			product999 = constuctImage(promoResponse, product, prods);
			promoAtProd[index] = product999;
			index += 1;
			prods.setDiscountedPrice(999);
			promoResponse.setAt999(prods);
		}
		while (productBogo.equals("mayurag")) {
			prods = new ProductDTO();
			Page<ProductEntity> product = prodRepository.findProdPromo(promoAtProd, productBogo, pageable);
			productBogo = constuctImage(promoResponse, product, prods);
			carousel.add(prods);
		}
		while (productDotd.isEmpty()) {
			prods = new ProductDTO();
			Page<ProductEntity> product = prodRepository.findProdPromo(promoAtProd, productBogo, pageable);
			productDotd = constuctImage(promoResponse, product, prods);
			prods.setDiscountedPrice(prods.getRetailPrice() / 10);
			carousel.add(prods);
		}
		promoRepository.updatePromo("promo_mayur28", product99, product499, product999, productBogo, productDotd,
				today);
		promoResponse.setCarouselPromo(carousel);
		return promoResponse;
	}

	private String constuctImage(PromotionResponse promoResponse, Page<ProductEntity> product, ProductDTO prods)
			throws IOException {
		String productName = "";
		for (ProductEntity prod : product.getContent()) {
			boolean isValid = false;
			String[] images = prod.getProdImage().split(",");
			for (String image : images) {
				image = image.replace("[", "").replace("]", "");
				image = image.replace("\"", "").replace("\"", "");
				image = image.replace("http://img5a", "https://rukminim1").replace("http://img6a", "https://rukminim1")
						.trim();
				isValid = validateUrl(image);
				if (isValid) {
					productName = prod.getProductName();
					prod.setProdImage(image);
					BeanUtils.copyProperties(prod, prods);
					break;
				}
			}
			if (isValid)
				break;
		}
		return productName;
	}

	private boolean validateUrl(String prodUrl) throws IOException {
		URL url = new URL(prodUrl);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setInstanceFollowRedirects(false);
		int responseCode = huc.getResponseCode();
		if (responseCode == 200) {
			return true;
		} else {
			return false;
		}
	}

}
