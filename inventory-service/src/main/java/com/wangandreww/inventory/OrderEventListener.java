package com.wangandreww.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderEventListener {

	private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

	private final ProductRepository products;
	private final KafkaTemplate<String, InventoryEvent> kafka;

	public OrderEventListener(ProductRepository products, KafkaTemplate<String, InventoryEvent> kafka) {
		this.products = products;
		this.kafka = kafka;
	}

	// Called once per event. The offset is committed after this returns, so a crash mid-way means redelivery.
	@KafkaListener(topics = "order-events")
	@Transactional
	public void onOrderPlaced(OrderPlacedEvent event) {
		InventoryEvent result = reserve(event);
		// Keyed by order so every result for one order stays in order on the same partition
		kafka.send(KafkaTopicConfig.INVENTORY_EVENTS, String.valueOf(event.orderId()), result);
	}

	private InventoryEvent reserve(OrderPlacedEvent event) {
		Product product = products.findById(event.productId()).orElse(null);
		if (product == null) {
			log.warn("Order {}: unknown product {}", event.orderId(), event.productId());
			return InventoryEvent.rejected(event.orderId(), "Unknown product " + event.productId());
		}

		if (product.tryDeduct(event.quantity())) {
			// No save() needed: changes to a loaded entity are written when the transaction commits
			log.info("Order {}: deducted {} x {}, {} left", event.orderId(), event.quantity(),
					product.getId(), product.getStock());
			return InventoryEvent.reserved(event.orderId());
		}

		log.warn("Order {}: not enough {} (wanted {}, have {})", event.orderId(), product.getId(),
				event.quantity(), product.getStock());
		return InventoryEvent.rejected(event.orderId(),
				"Not enough " + product.getId() + " in stock (have " + product.getStock() + ")");
	}

}
