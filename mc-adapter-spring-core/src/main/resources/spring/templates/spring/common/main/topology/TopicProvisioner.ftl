package ${TopicProvisioner.packageName()};


import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
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

            client.createTopics(standardTopics).all().get(10, TimeUnit.SECONDS);
        }
        catch (java.util.concurrent.ExecutionException e) {
            if (!(e.getCause() instanceof TopicExistsException)) {
                log.warn("Topic creation encountered an error: {}", e.getMessage());
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Topic creation was interrupted: {}", e.getMessage());
        }
        catch (java.util.concurrent.TimeoutException e) {
            log.warn("Topic creation timed out: {}", e.getMessage());
        }
    }
}
