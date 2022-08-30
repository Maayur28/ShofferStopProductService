package com.prodservice.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prodservice.entity.ProductEntity;
import com.prodservice.entity.PromotionEntity;
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

	@Override
	@Cacheable(cacheNames = "promotion")
	public PromotionResponse getPromotion() throws IOException {
		PromotionResponse promoResponse = new PromotionResponse();

		PromotionEntity promo = promoRepository.findByPromoId("promo_mayur28");
		LocalDate today = LocalDate.now().plusDays(4);
		if (promo == null) {
			promo = new PromotionEntity();
			promo.setPromoId("promo_mayur28");
			promo.setBogo("bogo");
			promo.setDotd("dotd");
			promo.setAt99("99");
			promo.setAt499("499");
			promo.setAt999("999");
			promo.setPromoDate(today.toString());
			promoRepository.save(promo);
		} else {
			LocalDate oldDate = LocalDate.parse(promo.getPromoDate());
			Period diff = Period.between(oldDate, today);
			if (diff.getDays() > 0 || diff.getMonths() > 0 || diff.getYears() > 0) {
				promoResponse = constructPromotion(promoResponse, today.toString());
			} else {
				promoResponse = getPromotion(promoResponse, promo);
			}
		}
		return promoResponse;
	}

	@CachePut(value = "promotion", key = "promo")
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

	@Cacheable(value = "promotion", key = "promo")
	private PromotionResponse getPromotion(PromotionResponse promoResponse, PromotionEntity promo) throws IOException {
		List<String> prods = new ArrayList<>();
		prods.add(promo.getAt99());
		prods.add(promo.getAt499());
		prods.add(promo.getAt999());
		prods.add(promo.getBogo());
		prods.add(promo.getDotd());
		int i = 0;
		List<ProductDTO> carousel = new ArrayList<>();
		for (String prod : prods) {
			ProductEntity product = prodRepository.findProductByProductName(prod);
			boolean isValid = false;
			String[] images = product.getProdImage().split(",");
			for (String image : images) {
				image = image.replace("[", "").replace("]", "");
				image = image.replace("\"", "").replace("\"", "");
				image = image.replace("http://img5a", "https://rukminim1").replace("http://img6a", "https://rukminim1")
						.trim();
				isValid = validateUrl(image);
				if (isValid) {
					product.setProdImage(image);
					ProductDTO prodds = new ProductDTO();
					BeanUtils.copyProperties(product, prodds);
					if (i == 0) {
						prodds.setDiscountedPrice(99);
						promoResponse.setAt99(prodds);
					}
					if (i == 1) {
						prodds.setDiscountedPrice(499);
						promoResponse.setAt499(prodds);
					}
					if (i == 2) {
						prodds.setDiscountedPrice(999);
						promoResponse.setAt999(prodds);
					}
					if (i == 3) {
						carousel.add(prodds);
					}
					if (i == 4) {
						prodds.setDiscountedPrice(prodds.getRetailPrice() / 10);
						carousel.add(prodds);
					}
					i++;
					break;
				}
			}
		}
		promoResponse.setCarouselPromo(carousel);
		return promoResponse;
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
