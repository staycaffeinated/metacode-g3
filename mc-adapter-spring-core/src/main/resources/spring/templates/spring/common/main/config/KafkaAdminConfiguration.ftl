<#include "/common/Copyright.ftl">

package ${KafkaAdminConfiguration.packageName()};

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * KafkaAdmin configuration. Topics are provisioned via TopicProvisioner.
 * KafkaAdmin bean is auto-configured by Spring Boot from `spring.kafka.admin.*` properties.
 */
@Configuration
@EnableKafka
public class ${KafkaAdminConfiguration.className()} {}