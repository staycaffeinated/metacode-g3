<#include "/common/Copyright.ftl">

package ${KafkaTopicsConfiguration.packageName()};

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

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

    <#noparse>@Value("${spring.kafka.bootstrap-servers}")</#noparse>
    private List<String> bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(AdminClientConfig.CLIENT_ID_CONFIG, "topic-manager");
        return new KafkaAdmin(configs);
    }

}