package com.kafka.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.kafka.consumer.vo.VehicleData;

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

		@KafkaListener(topics = "${vehicleData.topic.name}",  groupId = "vehicleData", containerFactory = "vehicleDataKafkaListenerContainerFactory")
		public void vehicleDataListener(VehicleData VehicleData) {
			System.out.println("Recieved message: " + VehicleData);
			this.vehicleDataLatch.countDown();
		}

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
