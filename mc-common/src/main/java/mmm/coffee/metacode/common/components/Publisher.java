package mmm.coffee.metacode.common.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Publisher {
    private final ApplicationEventPublisher eventPublisher;

    public Publisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishBasePackageAssigned(final String basePackage) {
        log.info("Publishing base package assigned: {}", basePackage);
        eventPublisher.publishEvent(new BasePackageAssignedEvent(this, basePackage));
    }

}
