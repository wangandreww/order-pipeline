package com.wangandreww.order;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Rejects orders whose inventory reply never arrived (lost event, inventory down too long, a bug)
@Component
public class OrderTimeoutJob {

	private static final Logger log = LoggerFactory.getLogger(OrderTimeoutJob.class);

	private final OrderRepository orders;
	private final Duration timeout;

	public OrderTimeoutJob(OrderRepository orders, @Value("${orders.placed-timeout:2m}") Duration timeout) {
		this.orders = orders;
		this.timeout = timeout;
	}

	// fixedDelay: wait 30s after each run finishes, so runs never overlap
	@Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
	@Transactional
	public void rejectStaleOrders() {
		Instant cutoff = Instant.now().minus(timeout);
		for (Order order : orders.findByStatusAndCreatedAtBefore(Order.Status.PLACED, cutoff)) {
			// Known gap: if inventory replies after this, it has deducted stock for a rejected order.
			// A full saga would publish a cancellation so inventory can put the stock back.
			order.reject("Timed out waiting for inventory");
			log.warn("Order {} timed out after {}", order.getId(), timeout);
		}
	}

}
