package com.wangandreww.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// The JSON body a client sends to POST /orders
public record CreateOrderRequest(
		@NotBlank String productId,
		@NotNull @Positive Integer quantity) {
}
