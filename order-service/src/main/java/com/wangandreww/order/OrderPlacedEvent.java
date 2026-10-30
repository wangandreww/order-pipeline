package com.wangandreww.order;

// Published to Kafka when an order is saved. Other services depend on this shape, so change it carefully.
public record OrderPlacedEvent(
		Long orderId,
		String productId,
		int quantity) {

	static OrderPlacedEvent from(Order order) {
		return new OrderPlacedEvent(order.getId(), order.getProductId(), order.getQuantity());
	}

}
