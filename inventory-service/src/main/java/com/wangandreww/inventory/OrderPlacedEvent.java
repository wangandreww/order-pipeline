package com.wangandreww.inventory;

// Mirrors the JSON that order-service publishes to order-events. The services share the JSON shape, not this class.
public record OrderPlacedEvent(
		Long orderId,
		String productId,
		int quantity) {
}
