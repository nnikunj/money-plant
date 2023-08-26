package com.example.rmqconnector;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class RmqUtils {

	private final RabbitTemplate rabbitTemplate;

	public void send(String queueName, Object message) {
		log.debug("Sending message to queue {}", queueName);
		RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
		// check if queue exists
		if (admin.getQueueInfo(queueName) == null) {
			log.debug("Queue {} not found. Creating a new queue with default exchange", queueName);
			createQueueWithName(queueName);
		}
		rabbitTemplate.convertAndSend(queueName, message);
	}

	public void send(final String queueName, Object message, final Map<String, String> mdsContext) {
		log.debug("Sending message to queue {}", queueName);
		RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
		// check if queue exists
		if (admin.getQueueInfo(queueName) == null) {
			log.debug("Queue {} not found. Creating a new queue with default exchange", queueName);
			createQueueWithName(queueName);
		}
		if (CollectionUtils.isEmpty(mdsContext)) {
			send(queueName, message);
			return;
		}
		rabbitTemplate.convertAndSend(queueName, message, messagePostProcessor -> {
			mdsContext.forEach((k, v) -> messagePostProcessor.getMessageProperties().setHeader(k, v));
			return messagePostProcessor;
		});
	}

	public void createQueueWithName(String queueName) {
		log.info("creating queue [{}]", queueName);
		RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
		if (admin.getQueueInfo(queueName) != null) {
			log.info("queue already exists [{}]", queueName);
			return;
		}

		Queue queue = new Queue(queueName, true, false, false);
		Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, "", queueName, null);
		admin.declareQueue(queue);
		admin.declareBinding(binding);
		log.info("created queue [{}]", queueName);
	}


	public boolean deleteQueueWithName(String queueName) {
		log.info("deleting queue: {}", queueName);
		RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);

		if (admin.getQueueInfo(queueName) != null)
			return admin.deleteQueue(queueName);

		log.info("The queue {} does not exists ", queueName);
		return true;
	}


}
