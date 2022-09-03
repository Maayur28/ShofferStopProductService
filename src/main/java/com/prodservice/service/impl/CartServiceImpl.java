package com.prodservice.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodservice.entity.CartEntity;
import com.prodservice.entity.GiftEntity;
import com.prodservice.entity.OrderEntity;
import com.prodservice.entity.OrderIdEntity;
import com.prodservice.entity.PromotionEntity;
import com.prodservice.entity.UserAddressEntity;
import com.prodservice.model.request.CartRequest;
import com.prodservice.model.request.CartUpdateRequest;
import com.prodservice.model.request.OrderRequest;
import com.prodservice.model.response.CartResponse;
import com.prodservice.repository.AddressRepository;
import com.prodservice.repository.CartRepository;
import com.prodservice.repository.GiftRepository;
import com.prodservice.repository.OrderEntityRepository;
import com.prodservice.repository.OrderRepository;
import com.prodservice.repository.ProdRepository;
import com.prodservice.repository.PromotionRepository;
import com.prodservice.service.CartService;
import com.prodservice.service.PromotionService;
import com.prodservice.shared.dto.CartDTO;
import com.prodservice.shared.dto.GiftDTO;
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

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	OrderEntityRepository orderEntityRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	GiftRepository giftRepository;

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
		List<GiftDTO> gifts = new ArrayList<>();
		List<CartEntity> cartEntity = cartRepository.findCartByuserId(userId);
		Collections.reverse(cartEntity);
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
					setCartProductPromo(item, promoResponse, gifts);
					Integer quantity = Integer.valueOf(item.getProductQuantity());
					item.setDiscountedPrice(item.getDiscountedPrice() * quantity);
					item.setRetailPrice(item.getRetailPrice() * quantity);
					totalDiscount += (item.getRetailPrice() - item.getDiscountedPrice());
					totalAfterDiscount += item.getDiscountedPrice();
					totalBeforeDiscount += item.getRetailPrice();
				}
				cartResponse.setItems(items);
				cartResponse.setTotalAfterDiscount(totalAfterDiscount);
				cartResponse.setTotalBeforeDiscount(totalBeforeDiscount);
				cartResponse.setTotalDiscount(totalDiscount);
				cartResponse.setGifts(gifts);
			}
		}
		return cartResponse;
	}

	private void setCartProductPromo(CartDTO item, PromotionEntity promoResponse, List<GiftDTO> gifts) {
		List<String> at99 = Arrays.asList(promoResponse.getAt99().split(","));
		List<String> at499 = Arrays.asList(promoResponse.getAt499().split(","));
		List<String> at999 = Arrays.asList(promoResponse.getAt999().split(","));
		if (item.getProductName().equalsIgnoreCase(promoResponse.getBogo())) {
			GiftDTO gift = new GiftDTO();
			gift.setProductBrand(item.getProductBrand());
			gift.setProductImage(item.getProductImage());
			gift.setProductName(item.getProductName());
			gift.setProductQuantity(item.getProductQuantity());
			item.setPromotionMessage("Buy One Get One");
			gifts.add(gift);
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

	@Override
	public CartResponse updateCart(CartUpdateRequest cartUpdateRequest, String userId) throws Exception {
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}
		if (cartUpdateRequest.getProductQuantity() != null
				&& Integer.valueOf(cartUpdateRequest.getProductQuantity()) > 5) {
			cartUpdateRequest.setProductQuantity("10");
		}
		cartRepository.updateCartByProductNameAndUserId(userId, cartUpdateRequest.getProductName(),
				cartUpdateRequest.getProductQuantity());
		return getCart(userId);
	}

	@Override
	public CartResponse deleteCart(String productName, String userId) throws Exception {
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}
		cartRepository.deleteCartByProductNameAndUserId(productName, userId);
		return getCart(userId);
	}

	@Override
	public String createOrder(OrderRequest orderRequest, String userId) throws Exception {
		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}

		OrderEntity orderEntity = new OrderEntity();

		// SaveAddress
		UserAddressEntity address = new UserAddressEntity();
		BeanUtils.copyProperties(orderRequest.getAddress(), address);
		address.setUserId(userId);
		address = addressRepository.save(address);

		CartResponse cartResponse = orderRequest.getCart();
		List<String> giftIds = new ArrayList<>();
		List<String> orderIds = new ArrayList<>();
		List<Long> cardIds = new ArrayList<>();

		// Save Gift
		for (GiftDTO gift : cartResponse.getGifts()) {
			GiftEntity giftEntity = new GiftEntity();
			BeanUtils.copyProperties(gift, giftEntity);
			giftEntity = giftRepository.save(giftEntity);
			giftIds.add(String.valueOf(giftEntity.getId()));
		}

		// Save Items
		for (CartDTO cart : cartResponse.getItems()) {
			cardIds.add(cart.getId());
			OrderIdEntity orderIdEntity = new OrderIdEntity();
			BeanUtils.copyProperties(cart, orderIdEntity);
			orderIdEntity = orderEntityRepository.save(orderIdEntity);
			orderIds.add(String.valueOf(orderIdEntity.getId()));
		}

		// Save Order
		orderEntity.setAddressId(String.valueOf(address.getId()));
		orderEntity.setUserId(userId);
		orderEntity.setTotalAfterDiscount(cartResponse.getTotalAfterDiscount());
		orderEntity.setTotalDiscount(cartResponse.getTotalDiscount());
		orderEntity.setTotalBeforeDiscount(cartResponse.getTotalBeforeDiscount());
		orderEntity.setOrderIds(String.join(",", orderIds));
		orderEntity.setGiftIds(String.join(",", giftIds));
		LocalDate date = LocalDate.now();
		String orderDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
		List<String> orderDateList = new ArrayList<>();
		orderDateList.add(orderDate);
		int i = 0;
		while (i < 3) {
			Random random = new Random();
			int num = random.nextInt(3 - 1) + 1;
			date = date.plusDays(num);
			String orderDates = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date);
			orderDateList.add(orderDates);
			i++;
		}

		orderEntity.setOrderDates(String.join("-", orderDateList));
		orderEntity.setDate(orderDate);
		orderEntity = orderRepository.save(orderEntity);
		cartRepository.deleteAllById(cardIds);

		return String.valueOf(orderEntity.getId());

	}
}
