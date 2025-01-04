<#include "/common/Copyright.ftl">

package ${KafkaTopicsConfiguration.packageName()};

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configure auto-created Kafka topics. This class is not necessary
 * if you don't want auto-created kafka topics.
 */
@Configuration
public class ${KafkaTopicsConfiguration.className()} {
    public static final String MESSAGE_TOPIC = "message-topic";

    @Bean
    public NewTopic messageTopic() {
        return TopicBuilder.name(MESSAGE_TOPIC).partitions(10).replicas(1).build();
    }
}