package com.wangandreww.order;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// Spring Data builds the query from the method name: newest 50 orders by id
	List<Order> findTop50ByOrderByIdDesc();

	// WHERE status = ? AND created_at < ?
	List<Order> findByStatusAndCreatedAtBefore(Order.Status status, Instant cutoff);

}
