package com.example.rmqconnector;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("rmq-csp-config")
@Getter
@Setter
public class ClientConfigProperties {
    private String authProvider;

    private String baseUrl;

    private String clientId;

    private String clientSecret;

    private String grantType;

    private String orgId;

    private double refreshCredentialStrategy = 0.8;
}
