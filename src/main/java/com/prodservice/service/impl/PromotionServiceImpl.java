package com.prodservice.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	PromotionRepository promoRepository;

	@Autowired
	ProdRepository prodRepository;

	@Override
	public PromotionResponse getPromotion() throws IOException, IllegalAccessException, InvocationTargetException {
		PromotionResponse promoResponse = new PromotionResponse();

		PromotionEntity promo = promoRepository.findByPromoId("promo_mayur28");
		LocalDate today = LocalDate.now();
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
				Pageable pageable = PageRequest.of(0, 5);
				boolean at99 = false, at499 = false, at999 = false, bogo = false, dotd = false;
				String product99 = "", product499 = "", product999 = "", productBogo = "", productDotd = "";
				String[] promoAtProd = new String[3];
				String prodBogo = "bogo";
				int index = 0;
				while (!at99) {
					Page<ProductEntity> at99Product = prodRepository.findPromoAt(99, 499, pageable);
					for (ProductEntity prod : at99Product.getContent()) {
						boolean isValid = false;
						String[] images = prod.getProdImage().split(",");
						for (String image : images) {
							image = image.replace("[", "").replace("]", "");
							image = image.replace("\"", "").replace("\"", "");
							image = image.replace("http://img5a", "https://rukminim1")
									.replace("http://img6a", "https://rukminim1").trim();
							isValid = validateUrl(image);
							if (isValid) {
								promoAtProd[index] = prod.getProductName();
								product99 = prod.getProductName();
								index += 1;
								at99 = true;
								prod.setProdImage(image);
								ProductDTO prods = new ProductDTO();
								BeanUtils.copyProperties(prods, prod);
								prods.setDiscountedPrice(99);
								promoResponse.setAt99(prods);
								break;
							}
						}
						if (isValid)
							break;
					}
				}
				while (!at499) {
					Page<ProductEntity> at499Product = prodRepository.findPromoAt(499, 999, pageable);
					for (ProductEntity prod : at499Product.getContent()) {
						boolean isValid = false;
						String[] images = prod.getProdImage().split(",");
						for (String image : images) {
							image = image.replace("[", "").replace("]", "");
							image = image.replace("\"", "").replace("\"", "");
							image = image.replace("http://img5a", "https://rukminim1")
									.replace("http://img6a", "https://rukminim1").trim();
							isValid = validateUrl(image);
							if (isValid) {
								promoAtProd[index] = prod.getProductName();
								product499 = prod.getProductName();
								index += 1;
								at499 = true;
								prod.setProdImage(image);
								ProductDTO prods = new ProductDTO();
								BeanUtils.copyProperties(prods, prod);
								prods.setDiscountedPrice(499);
								promoResponse.setAt499(prods);
								break;
							}
						}
						if (isValid)
							break;
					}
				}
				while (!at999) {
					Page<ProductEntity> at999Product = prodRepository.findPromoAt(999, 99999, pageable);
					for (ProductEntity prod : at999Product.getContent()) {
						boolean isValid = false;
						String[] images = prod.getProdImage().split(",");
						for (String image : images) {
							image = image.replace("[", "").replace("]", "");
							image = image.replace("\"", "").replace("\"", "");
							image = image.replace("http://img5a", "https://rukminim1")
									.replace("http://img6a", "https://rukminim1").trim();
							isValid = validateUrl(image);
							if (isValid) {
								promoAtProd[index] = prod.getProductName();
								product999 = prod.getProductName();
								index += 1;
								at999 = true;
								prod.setProdImage(image);
								ProductDTO prods = new ProductDTO();
								BeanUtils.copyProperties(prods, prod);
								prods.setDiscountedPrice(999);
								promoResponse.setAt999(prods);
								break;
							}
						}
						if (isValid)
							break;
					}
				}
				List<ProductDTO> carousel = new ArrayList<>();
				while (!bogo) {
					Page<ProductEntity> bogoProduct = prodRepository.findProdPromo(promoAtProd, prodBogo, pageable);
					for (ProductEntity prod : bogoProduct.getContent()) {
						boolean isValid = false;
						String[] images = prod.getProdImage().split(",");
						for (String image : images) {
							image = image.replace("[", "").replace("]", "");
							image = image.replace("\"", "").replace("\"", "");
							image = image.replace("http://img5a", "https://rukminim1")
									.replace("http://img6a", "https://rukminim1").trim();
							isValid = validateUrl(image);
							if (isValid) {
								prodBogo = prod.getProductName();
								productBogo = prod.getProductName();
								bogo = true;
								prod.setProdImage(image);
								ProductDTO prods = new ProductDTO();
								BeanUtils.copyProperties(prods, prod);
								carousel.add(prods);
								break;
							}
						}
						if (isValid)
							break;
					}
				}
				while (!dotd) {
					Page<ProductEntity> dotdProduct = prodRepository.findProdPromo(promoAtProd, prodBogo, pageable);
					for (ProductEntity prod : dotdProduct.getContent()) {
						boolean isValid = false;
						String[] images = prod.getProdImage().split(",");
						for (String image : images) {
							image = image.replace("[", "").replace("]", "");
							image = image.replace("\"", "").replace("\"", "");
							image = image.replace("http://img5a", "https://rukminim1")
									.replace("http://img6a", "https://rukminim1").trim();
							isValid = validateUrl(image);
							if (isValid) {
								dotd = true;
								prod.setProdImage(image);
								productDotd = prod.getProductName();
								ProductDTO prods = new ProductDTO();
								BeanUtils.copyProperties(prods, prod);
								prods.setDiscountedPrice(prods.getRetailPrice() / 10);
								carousel.add(prods);
								break;
							}
						}
						if (isValid)
							break;
					}
				}
				promoRepository.updatePromo("promo_mayur28", product99, product499, product999, productBogo,
						productDotd, today.toString());
				promoResponse.setCarouselPromo(carousel);
			} else {
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
						image = image.replace("http://img5a", "https://rukminim1")
								.replace("http://img6a", "https://rukminim1").trim();
						isValid = validateUrl(image);
						if (isValid) {
							product.setProdImage(image);
							ProductDTO prodds = new ProductDTO();
							BeanUtils.copyProperties(prodds, product);
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
			}
		}
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
