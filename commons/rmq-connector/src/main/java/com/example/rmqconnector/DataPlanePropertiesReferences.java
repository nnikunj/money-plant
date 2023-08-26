package com.example.rmqconnector;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties("dataplane-creds-ref")
@Getter
@Setter
public class DataPlanePropertiesReferences {
    private String operationalClientId;
    private String managementClientId;
}


