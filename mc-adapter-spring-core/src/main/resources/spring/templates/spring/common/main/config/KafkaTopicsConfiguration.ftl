<#include "/common/Copyright.ftl">

package ${KafkaTopicsConfiguration.packageName()};

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Configure any Kafka topics. This class is not necessary
 * if you don't want auto-created kafka topics.
 *
 * References:
 * https://docs.spring.io/spring-kafka/reference/kafka/configuring-topics.html
 * https://www.confluent.io/blog/how-choose-number-topics-partitions-kafka-cluster/
 * https://www.confluent.io/blog/kafka-streams-tables-part-2-topics-partitions-and-storage-fundamentals/
 */
@Configuration
@EnableKafka
public class ${KafkaTopicsConfiguration.className()} {
    // A hypothetical topic name. Change at will. 
    public static final String MESSAGE_TOPIC = "message-topic";

    <#noparse>@Value("${spring.kafka.bootstrap-servers}")</#noparse>
    private List<String> bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
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