package com.wangandreww.order;

import java.time.Instant;

// The JSON the API sends back, kept separate from the entity so the database shape isn't the API shape
public record OrderResponse(
		Long id,
		String productId,
		int quantity,
		Order.Status status,
		Instant createdAt) {

	static OrderResponse from(Order order) {
		return new OrderResponse(order.getId(), order.getProductId(), order.getQuantity(),
				order.getStatus(), order.getCreatedAt());
	}

}
