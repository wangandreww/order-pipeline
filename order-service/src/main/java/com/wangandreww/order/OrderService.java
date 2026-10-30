package com.wangandreww.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orders;
	private final KafkaTemplate<String, OrderPlacedEvent> kafka;

	public OrderService(OrderRepository orders, KafkaTemplate<String, OrderPlacedEvent> kafka) {
		this.orders = orders;
		this.kafka = kafka;
	}

	public Order placeOrder(String productId, int quantity) {
		Order order = orders.save(new Order(productId, quantity));

		// Keyed by product so all events for one product land in the same partition, in order.
		// If this send fails the order is already saved: the "dual write" problem, fixed later with an outbox.
		kafka.send(KafkaTopicConfig.ORDER_EVENTS, order.getProductId(), OrderPlacedEvent.from(order))
				.whenComplete((result, ex) -> {
					if (ex != null) {
						log.error("Failed to publish event for order {}", order.getId(), ex);
					}
					else {
						log.info("Published order {} to partition {} at offset {}", order.getId(),
								result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
					}
				});

		return order;
	}

}
