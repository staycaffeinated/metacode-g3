package mmm.coffee.metacode.common.components;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Getter
@Component
@Slf4j
public class BasePackageSupplier {
    private String basePackage;

    @EventListener
    void handleBasePackageAssignedEvent(BasePackageAssignedEvent event) {
        log.info("Received Event: {}", event);
        basePackage = event.getBasePackage();
    }
    
    public String basePackage() { return basePackage; }
}
