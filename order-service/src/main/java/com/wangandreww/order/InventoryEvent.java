package com.wangandreww.order;

// Mirrors the JSON that inventory-service publishes to inventory-events
public record InventoryEvent(
		Long orderId,
		Result result,
		String reason) {

	public enum Result {
		RESERVED, REJECTED
	}

}
