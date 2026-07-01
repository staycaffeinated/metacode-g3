<#include "/common/Copyright.ftl">
package ${KafkaConsumerConfiguration.packageName()};


import org.springframework.context.annotation.Configuration;

/**
 * Kafka consumer is auto-configured by Spring Boot from `spring.kafka.consumer.*` properties.
 * See application.yml for configuration values.
 */
@Configuration
public class ${KafkaConsumerConfiguration.className()} {}