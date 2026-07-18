<#include "/common/Copyright.ftl">
package ${KafkaConsumerConfiguration.packageName()};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka consumer is auto-configured by Spring Boot from `spring.kafka.consumer.*` properties.
 * See application.yml for configuration values.
 */
@Configuration
@Slf4j
public class ${KafkaConsumerConfiguration.className()} {
    private final ConsumerFactory<String, String> consumerFactory;
    private final int concurrency;

    public KafkaConsumerConfiguration(
            ConsumerFactory<String, String> consumerFactory,
            <#noparse>@Value("${spring.kafka.listener.concurrency:1}")</#noparse> int concurrency) {
        this.consumerFactory = consumerFactory;
        this.concurrency = concurrency;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(buildErrorHandler());
        return factory;
    }


    private DefaultErrorHandler buildErrorHandler() {
        var handler = new DefaultErrorHandler(
                (consumerRecord, exception) -> log.error(
                "Non-retriable error: topic={} partition={} offset={} key={}",
                consumerRecord.topic(),
                consumerRecord.partition(),
                consumerRecord.offset(),
                consumerRecord.key(),
                exception),
            new FixedBackOff(1_000L, 3L));
        handler.addNotRetryableExceptions(IllegalArgumentException.class, NullPointerException.class);
        return handler;
    }

}