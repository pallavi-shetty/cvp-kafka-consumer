package com.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import com.kafka.consumer.vo.VehicleData;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Pallavi Shetty
 * @since May 2020
 */

@SpringBootApplication
public class CvpKafkaConsumerApplication {

	private static final Logger logger = LoggerFactory.getLogger(CvpKafkaConsumerApplication.class);

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(CvpKafkaConsumerApplication.class, args);

		MessageListener listener = context.getBean(MessageListener.class);

		final AtomicBoolean closed = new AtomicBoolean(false);

		try {
			while (!closed.get()) {
				listener.vehicleDataLatch.await(10, TimeUnit.SECONDS);
			}
		} catch (WakeupException e) {
			// Ignore exception if closing
			if (!closed.get())
				throw e;
		} finally {
			context.close();
		}

	}

	@Bean
	public MessageListener messageListener() {
		return new MessageListener();
	}

	public static class MessageListener {

		private CountDownLatch vehicleDataLatch = new CountDownLatch(1);

		@KafkaListener(topics = "${kafka.consumer.topics.telemetryTopic}", groupId = "fleetman-geofence-service-group", containerFactory = "vehicleDataKafkaListenerContainerFactory")
		public void vehicleDataListener(VehicleData vehicleData) {
			System.out.println(
					"Recieved message: " + vehicleData.getVehicleId() + " eventDate " + vehicleData.getEventDateTime()
							+ " lattitude " + vehicleData.getLatitude() + " longitude " + vehicleData.getLongitude());

			logger.info(
					"Recieved message: " + vehicleData.getVehicleId() + " eventDate " + vehicleData.getEventDateTime()
							+ " lattitude " + vehicleData.getLatitude() + " longitude " + vehicleData.getLongitude());
			this.vehicleDataLatch.countDown();
		}

		/*
		 * @KafkaListener(topics = "${vehicleData.topic.name}", groupId = "vehicleData",
		 * containerFactory = "vehicleDataKafkaListenerContainerFactory") public void
		 * listen(@Payload String json) throws IOException { VehicleData telemetryData =
		 * this.mapper.readValue(json, VehicleData.class);
		 * logger.debug("Telemetry Message received : " + telemetryData);
		 * 
		 * 
		 * }
		 */

//		@KafkaListener(
//				topics = "${vehicleData.topic.name}",
//		        groupId = "vehicleData",
//		        containerFactory = "vehicleDataKafkaListenerContainerFactory",
//		        autoStartup = "true")
//		public void consumeFromCoreTopicPartitionZERO(@Payload List<VehicleData> containers){
//		    logger.info("\n/******* Consume TEST-TOPIC Partition ---->>>>>>    ONE ********/\n" +containers);
//		} 
	}

}
