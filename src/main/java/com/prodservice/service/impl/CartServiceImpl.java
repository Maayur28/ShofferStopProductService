package com.prodservice.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodservice.entity.CartEntity;
import com.prodservice.entity.PromotionEntity;
import com.prodservice.model.request.CartRequest;
import com.prodservice.model.response.CartResponse;
import com.prodservice.repository.CartRepository;
import com.prodservice.repository.ProdRepository;
import com.prodservice.repository.PromotionRepository;
import com.prodservice.service.CartService;
import com.prodservice.service.PromotionService;
import com.prodservice.shared.dto.CartDTO;
import com.prodservice.utils.ErrorMessages;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	PromotionRepository promoRepository;

	@Autowired
	ProdRepository prodRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	PromotionService promoService;
	
	@Override
	public long getCartCount(String userId) throws Exception {
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}
		long count = cartRepository.count();
		return count;
	}
	

	@Override
	public CartResponse getCart(String userId) throws IOException {
		CartResponse cartResponse = new CartResponse();
		List<CartEntity> cartEntity = cartRepository.findCartByuserId(userId);
		if (cartEntity != null) {
			List<CartDTO> items = new ArrayList<>();
			Integer totalBeforeDiscount = 0, totalAfterDiscount = 0, totalDiscount = 0;
			String[] cartProducts = new String[cartEntity.size()];
			int i = 0;
			for (CartEntity cart : cartEntity) {
				CartDTO cartDTO = new CartDTO();
				BeanUtils.copyProperties(cart, cartDTO);
				items.add(cartDTO);
				cartProducts[i] = cart.getProductName();
				i++;
			}
			if (i != 0) {
				PromotionEntity promoResponse = promoRepository.findByPromoId("promo_mayur28");
				if (promoResponse == null || !promoResponse.getPromoDate().equals(LocalDate.now().toString())) {
					promoService.clearPromotion();
					promoService.getPromotion();
					promoResponse = promoRepository.findByPromoId("promo_mayur28");
				}
				for (CartDTO item : items) {
					setCartProductPromo(item, promoResponse);
					totalDiscount += item.getRetailPrice() - item.getDiscountedPrice();
					totalAfterDiscount += item.getDiscountedPrice();
					totalBeforeDiscount += item.getRetailPrice();
				}
				cartResponse.setItems(items);
				cartResponse.setTotalAfterDiscount(totalAfterDiscount);
				cartResponse.setTotalBeforeDiscount(totalBeforeDiscount);
				cartResponse.setTotalDiscount(totalDiscount);
			}
		}
		return cartResponse;
	}

	private void setCartProductPromo(CartDTO item, PromotionEntity promoResponse) {
		List<String> at99 = Arrays.asList(promoResponse.getAt99().split(","));
		List<String> at499 = Arrays.asList(promoResponse.getAt499().split(","));
		List<String> at999 = Arrays.asList(promoResponse.getAt999().split(","));
		if (item.getProductName().equalsIgnoreCase(promoResponse.getBogo())) {
			item.setPromotionMessage("Buy One Get One");
		} else if (item.getProductName().equalsIgnoreCase(promoResponse.getDotd())) {
			item.setPromotionMessage("Flash Deal");
			item.setDiscountedPrice(item.getRetailPrice() / 10);
		} else if (at99.contains(item.getProductName())) {
			item.setPromotionMessage("At ₹99 Only");
			item.setDiscountedPrice(99);
		} else if (at499.contains(item.getProductName())) {
			item.setPromotionMessage("At ₹499 Only");
			item.setDiscountedPrice(499);
		} else if (at999.contains(item.getProductName())) {
			item.setPromotionMessage("At ₹999 Only");
			item.setDiscountedPrice(999);
		}
	}

	@Override
	public long createCart(CartRequest cartRequest, String userId) throws Exception {
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}

		CartEntity cartEntity = new CartEntity();
		cartEntity.setUserId(userId);
		cartEntity.setDiscountedPrice(cartRequest.getDiscountedPrice());
		cartEntity.setRetailPrice(cartRequest.getRetailPrice());
		cartEntity.setProductBrand(cartRequest.getProductBrand());
		cartEntity.setProductImage(cartRequest.getProductImage());
		cartEntity.setProductName(cartRequest.getProductName());
		cartEntity.setProductQuantity(cartRequest.getQuantity().toString());

		CartEntity cartRes = cartRepository.findCartByProductNameAndUserId(userId, cartRequest.getProductName());

		if (cartRes == null) {
			cartRepository.save(cartEntity);
		} else {
			cartRepository.updateCartByProductNameAndUserId(userId, cartRequest.getProductImage(),
					cartRequest.getQuantity().toString());
		}
		long count = cartRepository.count();
		return count;
	}

}
