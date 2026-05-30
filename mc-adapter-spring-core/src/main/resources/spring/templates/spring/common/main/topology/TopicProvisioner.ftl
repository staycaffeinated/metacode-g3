package ${TopicProvisioner.packageName()};


import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

/*
 * This is a convenience class that creates the standard topics used by the application.
 * Auto-creating topics and state stores is usually acceptable for local development.
 * Production environments usually have Kubernetes, Terraform or an equivalent tool
 * create the topics.
 */

@Component
@Slf4j
public class ${TopicProvisioner.className()} {

    private final KafkaAdmin kafkaAdmin;

    @Autowired
    public TopicProvisioner(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @PostConstruct
    public void createStandardTopics() {
        try (AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            List<NewTopic> standardTopics = Schema.allTopics().stream()
                .map(topic -> new NewTopic(topic, 4, (short) 1))
                .toList();

            client.createTopics(standardTopics);
        }
    }
}
