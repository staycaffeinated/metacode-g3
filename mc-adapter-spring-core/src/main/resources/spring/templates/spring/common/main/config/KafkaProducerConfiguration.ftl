<#include "/common/Copyright.ftl">

package ${KafkaProducerConfiguration.packageName()};


import org.springframework.context.annotation.Configuration;

/**
 * Kafka producer is auto-configured by Spring Boot from `spring.kafka.producer.*` properties.
 * See application.yml for configuration values.
 */
@Configuration
public class ${KafkaProducerConfiguration.className()} {}