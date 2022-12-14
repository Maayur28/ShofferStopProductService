package com.prodservice.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prodservice.entity.GiftEntity;
import com.prodservice.entity.OrderEntity;
import com.prodservice.entity.OrderIdEntity;
import com.prodservice.entity.UserAddressEntity;
import com.prodservice.model.request.UserCreateAddressRequestDTO;
import com.prodservice.model.response.OrdersResponse;
import com.prodservice.model.response.Pagination;
import com.prodservice.repository.AddressRepository;
import com.prodservice.repository.CartRepository;
import com.prodservice.repository.GiftRepository;
import com.prodservice.repository.OrderEntityRepository;
import com.prodservice.repository.OrderRepository;
import com.prodservice.repository.ProdRepository;
import com.prodservice.repository.PromotionRepository;
import com.prodservice.service.OrderService;
import com.prodservice.service.PromotionService;
import com.prodservice.shared.dto.GiftDTO;
import com.prodservice.shared.dto.OrderItemResponse;
import com.prodservice.shared.dto.OrderResponseDTO;
import com.prodservice.utils.ErrorMessages;

@Service
public class OrderServiceImpl implements OrderService {

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
	public OrdersResponse getOrders(String userId, int page, int pageSize) throws Exception {
		OrdersResponse ordersResponse = new OrdersResponse();

		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
		Page<OrderEntity> orderEntity = orderRepository.findPaginatedByUserId(userId, pageable);
		if (orderEntity != null) {
			List<OrderItemResponse> orderItems = new ArrayList<>();
			List<GiftDTO> giftResponse = new ArrayList<>();
			List<OrderResponseDTO> itemResponse = new ArrayList<>();
			for (OrderEntity order : orderEntity.getContent()) {
				OrderItemResponse orderItemResponse = new OrderItemResponse();
				List<String> giftIds = new ArrayList<>(Arrays.asList(order.getGiftIds().split(",")));
				List<String> orderItemIds = new ArrayList<>(Arrays.asList(order.getOrderIds().split(",")));
				orderItemResponse.setDate(order.getDate());
				orderItemResponse.setTotalPrice(order.getTotalAfterDiscount());
				orderItemResponse.setId(order.getId());

				if (giftIds.size() > 0 && !giftIds.get(0).isEmpty()) {
					giftResponse = covertToGiftResponse(giftIds);
				}
				if (orderItemIds.size() > 0 && !orderItemIds.get(0).isEmpty()) {
					itemResponse = covertToItemResponse(orderItemIds);
				}
				String fullName = addressRepository.findFullNameById(Long.parseLong(order.getAddressId()));
				orderItemResponse.setFullName(fullName);
				orderItemResponse.setGifts(giftResponse);
				orderItemResponse.setItems(itemResponse);
				orderItems.add(orderItemResponse);
			}
			Pagination pagination = new Pagination();
			pagination.setPage(orderEntity.getPageable().getPageNumber() + 1);
			pagination.setPageSize(orderEntity.getPageable().getPageSize());
			ordersResponse.setTotal(orderEntity.getTotalElements());
			ordersResponse.setPagination(pagination);
			ordersResponse.setOrderItems(orderItems);
		}
		return ordersResponse;
	}

	@Override
	public OrderItemResponse getOrderById(String orderId, String userId) throws Exception {
		OrderItemResponse orderItemResponse = new OrderItemResponse();

		if (userId == null) {
			JSONObject obj = new JSONObject();
			obj.put("error", ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage().toString());
			throw new Exception(obj.toString());
		}
		OrderEntity orderEntity = orderRepository.findOrderByUserId(userId, Long.parseLong(orderId));

		if (orderEntity != null) {
			List<OrderItemResponse> orderItems = new ArrayList<>();
			List<GiftDTO> giftResponse = new ArrayList<>();
			List<OrderResponseDTO> itemResponse = new ArrayList<>();
			List<String> giftIds = new ArrayList<>(Arrays.asList(orderEntity.getGiftIds().split(",")));
			List<String> orderItemIds = new ArrayList<>(Arrays.asList(orderEntity.getOrderIds().split(",")));
			orderItemResponse.setDate(orderEntity.getDate());
			orderItemResponse.setTotalPrice(orderEntity.getTotalAfterDiscount());
			orderItemResponse.setId(orderEntity.getId());
			if (giftIds.size() > 0 && !giftIds.get(0).isEmpty()) {
				giftResponse = covertToGiftResponse(giftIds);
			}
			if (orderItemIds.size() > 0 && !orderItemIds.get(0).isEmpty()) {
				itemResponse = covertToItemResponse(orderItemIds);
			}
			UserAddressEntity addressEntity = addressRepository
					.findAddressById(Long.parseLong(orderEntity.getAddressId()));
			orderItemResponse.setFullName(addressEntity.getFullName());
			UserCreateAddressRequestDTO address = new UserCreateAddressRequestDTO();
			BeanUtils.copyProperties(addressEntity, address);
			orderItemResponse.setAddress(address);
			orderItemResponse.setGifts(giftResponse);
			orderItemResponse.setItems(itemResponse);
			LocalDate todayDate = LocalDate.now();
			if (orderEntity.getOrderDates() != null) {
				List<String> orderDatesStatus = new ArrayList<>();
				List<String> dates = Arrays.asList(orderEntity.getOrderDates().split("-"));
				orderItemResponse.setOrderDates(dates);
				for (String date : dates) {
					if (LocalDate.parse(date, DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
							.isBefore(todayDate)) {
						orderDatesStatus.add("-1");

					} else if (LocalDate.parse(date, DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
							.isEqual(todayDate)) {
						orderDatesStatus.add("0");
					} else {
						orderDatesStatus.add("1");
					}
				}
				orderItemResponse.setOrderDatesStatus(orderDatesStatus);
				System.out.println(orderDatesStatus);
			}
			orderItems.add(orderItemResponse);
		}
		return orderItemResponse;
	}

	private List<GiftDTO> covertToGiftResponse(List<String> giftIds) {
		List<GiftDTO> gifts = new ArrayList<>();
		long[] giftArr = new long[giftIds.size()];
		int i = 0;
		for (String gift : giftIds) {
			giftArr[i++] = Long.parseLong(gift);
		}
		List<GiftEntity> giftResponse = giftRepository.findGiftsByIds(giftArr);
		if (giftResponse.size() > 0) {
			for (GiftEntity giftRes : giftResponse) {
				GiftDTO gift = new GiftDTO();
				BeanUtils.copyProperties(giftRes, gift);
				gifts.add(gift);
			}
		}
		return gifts;
	}

	private List<OrderResponseDTO> covertToItemResponse(List<String> orderItemIds) {
		List<OrderResponseDTO> items = new ArrayList<>();
		long[] itemArr = new long[orderItemIds.size()];
		int i = 0;
		for (String item : orderItemIds) {
			itemArr[i++] = Long.parseLong(item);
		}
		List<OrderIdEntity> itemResponse = orderEntityRepository.findItemsByIds(itemArr);
		if (itemResponse.size() > 0) {
			for (OrderIdEntity itemRes : itemResponse) {
				OrderResponseDTO item = new OrderResponseDTO();
				BeanUtils.copyProperties(itemRes, item);
				items.add(item);
			}
		}
		return items;
	}
}
