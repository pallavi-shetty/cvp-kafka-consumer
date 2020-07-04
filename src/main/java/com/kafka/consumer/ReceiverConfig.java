package com.kafka.consumer;

import java.util.Map;
import java.util.HashMap;
import com.kafka.consumer.vo.VehicleData;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.common.serialization.StringDeserializer;
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

	@Value(value = "${kafka.bootstrapServers}")
	private String bootstrapAddress;

	@Value("${kafka.bootstrapServers}")
	private String bootstrapServers;

	@Value("${kafka.consumer.groupId}")
	private String groupId;

	@Value("${kafka.consumer.clientId}")
	private String consumerClientId;

	@Value("${kafka.consumer.autoResetConfig}")
	private String autoResetConfig;

    private String clientIdPrefix = RandomStringUtils.randomAlphabetic(4);
    
	//private String clientIdPrefix = "XYZD";

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	/*
	 * kafka.bootstrapServers=localhost:9090,localhost:9091,localhost:9092
	 * kafka.zookeeper=localhost:2181
	 * kafka.consumer.topics.telemetryTopic=microlise-raw-telemetry-v1
	 * kafka.consumer.clientId=fleetman-geofence-consumer-client
	 * kafka.producer.clientId=fleetman-geofence-producer-service
	 * kafka.consumer.groupId=fleetman-geofence-service-group
	 * kafka.consumer.autoResetConfig=latest
	 * 
	 */
	@Bean
	public ConsumerFactory<String, VehicleData> vehicleDataConsumerFactory() {
		JsonDeserializer<VehicleData> deserializer = new JsonDeserializer<>(VehicleData.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerClientId + "-" + clientIdPrefix);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoResetConfig);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
		props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.kafka.consumer.vo.VehicleData");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		// return new DefaultKafkaConsumerFactory<>(props);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, VehicleData> vehicleDataKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, VehicleData> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(vehicleDataConsumerFactory());
		factory.setBatchListener(true);
		return factory;
	}

}