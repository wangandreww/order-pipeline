package com.wangandreww.inventory;

// Published to inventory-events after handling an order, so order-service can confirm or reject it
public record InventoryEvent(
		Long orderId,
		Result result,
		String reason) {

	public enum Result {
		RESERVED, REJECTED
	}

	static InventoryEvent reserved(Long orderId) {
		return new InventoryEvent(orderId, Result.RESERVED, null);
	}

	static InventoryEvent rejected(Long orderId, String reason) {
		return new InventoryEvent(orderId, Result.REJECTED, reason);
	}

}
