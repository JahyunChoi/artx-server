package com.artx.artx.delivery.service;

import com.artx.artx.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

//	public List<Delivery> getDeliveryByOrderId(String orderId) {
//		return deliveryRepository.findAllByOrder_Id(orderId);
//	}
}
