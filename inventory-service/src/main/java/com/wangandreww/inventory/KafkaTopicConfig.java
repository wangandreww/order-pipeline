package com.wangandreww.inventory;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	public static final String INVENTORY_EVENTS = "inventory-events";

	// inventory-service publishes to this topic, so it owns creating it
	@Bean
	NewTopic inventoryEventsTopic() {
		return TopicBuilder.name(INVENTORY_EVENTS)
				.partitions(3)
				.replicas(1) // only one broker locally
				.build();
	}

}
