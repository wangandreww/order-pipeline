package com.wangandreww.order;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;
	private final OrderRepository orders;

	public OrderController(OrderService orderService, OrderRepository orders) {
		this.orderService = orderService;
		this.orders = orders;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
		Order order = orderService.placeOrder(request.productId(), request.quantity());
		return ResponseEntity
				.created(URI.create("/orders/" + order.getId()))
				.body(OrderResponse.from(order));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
		return ResponseEntity.of(orders.findById(id).map(OrderResponse::from));
	}

}
