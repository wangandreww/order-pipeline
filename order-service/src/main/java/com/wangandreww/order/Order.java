package com.wangandreww.order;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders") // "order" is a reserved word in SQL
public class Order {

	public enum Status {
		PLACED, CONFIRMED, REJECTED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String productId;

	@Column(nullable = false)
	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	// Only set when inventory rejects the order
	private String rejectionReason;

	// Required by JPA, which creates the object first and then fills in the fields
	protected Order() {
	}

	public Order(String productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
		this.status = Status.PLACED;
		this.createdAt = Instant.now();
	}

	// Only a PLACED order can move on. Returns false if it already did, so a redelivered event changes nothing.
	public boolean confirm() {
		return moveFromPlaced(Status.CONFIRMED);
	}

	public boolean reject(String reason) {
		if (!moveFromPlaced(Status.REJECTED)) {
			return false;
		}
		rejectionReason = reason;
		return true;
	}

	private boolean moveFromPlaced(Status next) {
		if (status != Status.PLACED) {
			return false;
		}
		status = next;
		return true;
	}

	public Long getId() {
		return id;
	}

	public String getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public Status getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

}
