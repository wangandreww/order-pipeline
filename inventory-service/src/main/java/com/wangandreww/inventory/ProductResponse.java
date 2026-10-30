package com.wangandreww.inventory;

public record ProductResponse(
		String id,
		int stock) {

	static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getStock());
	}

}
