package com.example.rmqconnector;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("managedsvc.queues")
@Getter
@Setter
public class QueuesConfigProperties {
    /**
     *  don't change member variable "controlPlane". This property being used to configure queues in yml
     */

    Map<String,String> controlPlane = new HashMap<>();

    /**
     *  don't change member variable "exclusiveControlPlane". This property being used to configure queues in yml
     */

    Map<String,String> controlPlaneActiveConsumerQueues= new HashMap<>();

    /**
     *  don't change member variable "dataPlane". This property being used to configure queues in yml
     */

    Map<String,String> dataPlane = new HashMap<>();
}
