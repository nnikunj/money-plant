package com.example.rmqconnector;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class QueuesConfig {
    private RabbitAdmin rabbitAdmin;

    private QueuesConfigProperties configProperties;

    public QueuesConfig(@NonNull final RabbitTemplate rabbitTemplate, @NonNull final QueuesConfigProperties configProperties) {
        this.rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        this.configProperties = configProperties;

    }

    public void createQueues() {

        if(!CollectionUtils.isEmpty(this.configProperties.getControlPlane())) {
            createQueues(this.configProperties.getControlPlane(),  new HashMap<>());
        }
        if(!CollectionUtils.isEmpty(this.configProperties.getDataPlane())) {
            createQueues(this.configProperties.getDataPlane(),  new HashMap<>());
        }
        if(!CollectionUtils.isEmpty(this.configProperties.getControlPlaneActiveConsumerQueues())) {
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-single-active-consumer", true);
            createQueues(this.configProperties.getControlPlaneActiveConsumerQueues(), arguments);
        }
    }

    private void createQueues(Map<String,String> queues, Map<String, Object> arguments) {
        log.info(" Required queues in current applications : [{}]" , queues);
        queues.entrySet().stream().forEach(qConfig -> {
            // check if queue exists
            final String queueName = qConfig.getValue();
            if (rabbitAdmin.getQueueInfo(qConfig.getValue()) == null) {
                log.info("Queue [{}] not found. Creating a new queue with default exchange", queueName);
                Queue queue = new Queue(queueName, true, false, false, arguments);
                Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, "", queueName, null);
                rabbitAdmin.declareQueue(queue);
                rabbitAdmin.declareBinding(binding);
                log.info("Queue [{}] created with default exchange", queueName);
            }  else {
                log.info("Queue [{}] already exists.", queueName);
            }
        });
    }





}
