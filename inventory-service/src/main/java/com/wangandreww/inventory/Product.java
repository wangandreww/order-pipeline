package com.wangandreww.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

	// The product code itself (e.g. "ABC") is the key, so it's assigned by us rather than generated
	@Id
	private String id;

	@Column(nullable = false)
	private int stock;

	// Required by JPA
	protected Product() {
	}

	public Product(String id, int stock) {
		this.id = id;
		this.stock = stock;
	}

	// Returns false instead of going negative, so the caller decides what an out-of-stock order means
	public boolean tryDeduct(int quantity) {
		if (quantity > stock) {
			return false;
		}
		stock -= quantity;
		return true;
	}

	public String getId() {
		return id;
	}

	public int getStock() {
		return stock;
	}

}
