package com.kafka.consumer;

import java.util.Map;
import java.util.HashMap;
import com.kafka.consumer.vo.VehicleData;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;



/**
 * @author Pallavi Shetty
 * @since May 2020
 */

@EnableKafka
@Configuration
public class ReceiverConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${vehicleData.topic.name}")
	private String topic;

	@Bean
	public ConsumerFactory<String, VehicleData> vehicleDataConsumerFactory() {
		JsonDeserializer<VehicleData> deserializer = new JsonDeserializer<>(VehicleData.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> config = new HashMap<>();
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "vehicleData");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		// config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		// config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
		// config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);

		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, VehicleData> vehicleDataKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, VehicleData> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(vehicleDataConsumerFactory());
		factory.setBatchListener(true);
		return factory;
	}

}