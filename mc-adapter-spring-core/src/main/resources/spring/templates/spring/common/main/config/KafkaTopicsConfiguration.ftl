<#include "/common/Copyright.ftl">

package ${KafkaTopicsConfiguration.packageName()};

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;
import java.util.HashMap;

/**
 * Configure auto-created Kafka topics. This class is not necessary
 * if you don't want auto-created kafka topics.
 *
 * References:
 * https://docs.spring.io/spring-kafka/reference/kafka/configuring-topics.html
 * https://www.confluent.io/blog/how-choose-number-topics-partitions-kafka-cluster/
 * https://www.confluent.io/blog/kafka-streams-tables-part-2-topics-partitions-and-storage-fundamentals/
 */
@Configuration
public class ${KafkaTopicsConfiguration.className()} {
    // A hypothetical topic name. Change at will. 
    public static final String MESSAGE_TOPIC = "message-topic";

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }


    @Bean
    public NewTopic messageTopic() {
        /*
         * The brokers' default number of partitions and replicas will be used
         * if those values are not explicitly set here.
         * For guidance on choosing the number of partitions, see:
         * https://www.confluent.io/blog/how-choose-number-topics-partitions-kafka-cluster/
         * https://www.confluent.io/blog/kafka-streams-tables-part-2-topics-partitions-and-storage-fundamentals/
         */
        return TopicBuilder.name(MESSAGE_TOPIC).build();
    }
}