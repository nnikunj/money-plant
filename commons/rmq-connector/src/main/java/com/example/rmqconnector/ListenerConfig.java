package com.example.rmqconnector;

import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.DefaultCredentialsRefreshService;
import com.rabbitmq.client.impl.OAuth2ClientCredentialsGrantCredentialsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class ListenerConfig {


    private final ClientConfigProperties rmqOperatorClientConfigProperties;

    private final RabbitProperties rabbitProperties;

    private final static String ORG_ID = "orgId";

    private final static String CSP_AUTH_URI = "am/api/auth/authorize";

    private final static String CSP_URL_DELIM = "/";

    @Value("${rmq-connection.name:noname}")
    private String connectionName;

    private final QueuesConfigProperties controlPlaneQueues;


    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() throws Exception {

        RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBean.setPort(rabbitProperties.getPort());
        rabbitConnectionFactoryBean.setHost(rabbitProperties.getHost());
        rabbitConnectionFactoryBean.setVirtualHost(rabbitProperties.getVirtualHost());

        if(Objects.isNull(rmqOperatorClientConfigProperties.getAuthProvider()) || rmqOperatorClientConfigProperties.getAuthProvider().equals("true")){
            rabbitConnectionFactoryBean.setCredentialsProvider(credentialsProvider());
            rabbitConnectionFactoryBean.setCredentialsRefreshService(defaultCredentialsRefreshService());
        }
        if (Objects.equals(true,rabbitProperties.getSsl().getEnabled())) {
            rabbitConnectionFactoryBean.setUseSSL(rabbitProperties.getSsl().getEnabled());
            rabbitConnectionFactoryBean.setKeyStore(rabbitProperties.getSsl().getKeyStore());
            rabbitConnectionFactoryBean.setKeyStorePassphrase(rabbitProperties.getSsl().getKeyStorePassword());
            rabbitConnectionFactoryBean.setKeyStoreType(rabbitProperties.getSsl().getKeyStoreType());
            rabbitConnectionFactoryBean.setKeyStoreAlgorithm(rabbitProperties.getSsl().getKeyStoreAlgorithm());
            rabbitConnectionFactoryBean.setTrustStore(rabbitProperties.getSsl().getTrustStore());
            rabbitConnectionFactoryBean.setTrustStorePassphrase(rabbitProperties.getSsl().getTrustStorePassword());
            rabbitConnectionFactoryBean.setTrustStoreType(rabbitProperties.getSsl().getTrustStoreType());
            rabbitConnectionFactoryBean.setTrustStoreAlgorithm(rabbitProperties.getSsl().getTrustStoreAlgorithm());
            rabbitConnectionFactoryBean
                    .setSkipServerCertificateValidation(!rabbitProperties.getSsl().isValidateServerCertificate());
            rabbitConnectionFactoryBean.setEnableHostnameVerification(rabbitProperties.getSsl().getVerifyHostname());
        }
        rabbitConnectionFactoryBean.afterPropertiesSet();
        log.info(
                "RabbitConnectionFactoryBean properties: Host [{}], VirtualHost: [{}], Port: [{}], Ssl Enabled: [{}]",
                rabbitProperties.getHost(), rabbitProperties.getVirtualHost(), rabbitProperties.getPort(),
                Optional.ofNullable(rabbitProperties.getSsl().getEnabled()).orElse(false));
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitConnectionFactoryBean.getObject());
        cachingConnectionFactory.setConnectionNameStrategy(strategy -> connectionName());
        return cachingConnectionFactory;
    }

    private DefaultCredentialsRefreshService defaultCredentialsRefreshService() {
        return new DefaultCredentialsRefreshService.DefaultCredentialsRefreshServiceBuilder().scheduler(null)
                .refreshDelayStrategy(duration -> Duration.ofSeconds((long) ((double) duration.getSeconds()
                        * rmqOperatorClientConfigProperties.getRefreshCredentialStrategy())))
                .approachingExpirationStrategy(ttl -> true).build();
    }

    private CredentialsProvider credentialsProvider() {
        return new OAuth2ClientCredentialsGrantCredentialsProvider.OAuth2ClientCredentialsGrantCredentialsProviderBuilder()
                .tokenEndpointUri(String.join(CSP_URL_DELIM,rmqOperatorClientConfigProperties.getBaseUrl(),
                        CSP_AUTH_URI))
                .clientId(rmqOperatorClientConfigProperties.getClientId())
                .clientSecret(rmqOperatorClientConfigProperties.getClientSecret())
                .grantType(rmqOperatorClientConfigProperties.getGrantType())
                .parameter(ORG_ID, rmqOperatorClientConfigProperties.getOrgId()).build();
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(CachingConnectionFactory connectionFactory,
                                                                            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setMessageConverter(messageConverter);
        factory.setAfterReceivePostProcessors(messagePostProcessor -> listenerMessageProcessor(messagePostProcessor));
        factory.setBeforeSendReplyPostProcessors(messagePostProcessor -> messageProcessor(messagePostProcessor));
        factory.setConsumerTagStrategy(q -> connectionName());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplateBean(final ConnectionFactory connectionFactory,
                                             final Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        RetryTemplate retryTemplate = new RetryTemplate();
        RetryPolicy retryPolicy = new AlwaysRetryPolicy();
        retryTemplate.setRetryPolicy(retryPolicy);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        rabbitTemplate
                .setAfterReceivePostProcessors(messagePostProcessor -> listenerMessageProcessor(messagePostProcessor));
        rabbitTemplate.setBeforePublishPostProcessors(messagePostProcessor -> messageProcessor(messagePostProcessor));
        return rabbitTemplate;
    }


    /**
     * creates queues configured as part of application yml.
     * Note: hard coded queues in application will not be created automatically.
     * @param rabbitTemplate
     * @return
     */
    @Bean(initMethod = "createQueues")
    public QueuesConfig managedSvcQueuesConfig(final RabbitTemplate rabbitTemplate) {
        return new QueuesConfig(rabbitTemplate,  controlPlaneQueues);
    }

    private Message messageProcessor(final Message messagePostProcessor) {
//        messagePostProcessor.getMessageProperties().getHeaders().forEach((k, v) -> {
//            try {
//                ManagedSvcContext.addToContext(ContextConstants.getContextConstant(k), v);
//            }
//            catch (IllegalArgumentException ex) {
//                log.debug("Could not add message header {} to context", k);
//            }
//        });
        return messagePostProcessor;
    }


    private Message listenerMessageProcessor(final Message messagePostProcessor) {
//        messagePostProcessor.getMessageProperties().getHeaders().forEach((k, v) -> {
//            try {
//                ContextConstants contextConstants = ContextConstants.getContextConstant(k);
//                if (ContextConstants.B3.equals(contextConstants) && !ObjectUtils.isEmpty(v)) {
//                    String[] spilt = v.toString().split("-");
//                    ManagedSvcContext.addToContext(ContextConstants.TRACE_ID, spilt[0]);
//                }
//                ManagedSvcContext.addToContext(contextConstants, v);
//            }
//            catch (IllegalArgumentException ex) {
//                log.debug("Could not add message header {} to context", k);
//            }
//        });
        return messagePostProcessor;
    }
    private String connectionName() {
        return String.join("-",connectionName, ObjectUtils.getIdentityHexString(this));
    }

}
