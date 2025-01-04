<#include "/common/Copyright.ftl">

package ${KafkaConsumerConfiguration.packageName()};

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

/**
 * Configure Kafka message consumption
 */
@Configuration
public class ${KafkaConsumerConfiguration.className()} {

    <#noparse>
    @Value("${spring.kafka.producer.bootstrap-servers}")
    </#noparse>
    private List<String> bootstrapServers;

    <#noparse>
    @Value("{spring.application.name}")
    </#noparse>
    public String groupId;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    @SuppressWarnings({"java:S125" // allow code examples in comment blocks
    })
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
    KafkaTemplate<String, String> template) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);

        // Other options for DefaultErrorHandler are
        // new DefaultErrorHandler(new FixedBackOff(0L,0L));
        factory.setCommonErrorHandler(new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template)));
        return factory;
    }
}