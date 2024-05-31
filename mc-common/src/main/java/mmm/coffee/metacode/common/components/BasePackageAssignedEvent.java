package mmm.coffee.metacode.common.components;

import org.springframework.context.ApplicationEvent;

public class BasePackageAssignedEvent extends ApplicationEvent {
    private final String basePackage;

    public BasePackageAssignedEvent(Object source, String basePackage) {
        super(source);
        this.basePackage = basePackage;
    }

    public String getBasePackage() { return basePackage; }
}
