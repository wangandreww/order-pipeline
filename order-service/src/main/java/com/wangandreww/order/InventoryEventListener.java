package com.wangandreww.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InventoryEventListener {

	private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

	private final OrderRepository orders;

	public InventoryEventListener(OrderRepository orders) {
		this.orders = orders;
	}

	@KafkaListener(topics = "inventory-events")
	@Transactional
	public void onInventoryResult(InventoryEvent event) {
		Order order = orders.findById(event.orderId()).orElse(null);
		if (order == null) {
			log.warn("Inventory result for unknown order {}", event.orderId());
			return;
		}

		boolean changed = switch (event.result()) {
			case RESERVED -> order.confirm();
			case REJECTED -> order.reject();
		};

		if (changed) {
			log.info("Order {} is now {}{}", order.getId(), order.getStatus(),
					event.reason() != null ? " (" + event.reason() + ")" : "");
		}
		else {
			log.info("Order {} is already {}, ignoring duplicate {}", order.getId(), order.getStatus(),
					event.result());
		}
	}

}
