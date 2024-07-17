<#include "/common/Copyright.ftl">
package ${EntityEvent.packageName()};

import ${EntityResource.fqcn()};
import org.springframework.context.ApplicationEvent;

/**
 * ${endpoint.entityName} events
 */
 @SuppressWarnings({"Java:1102"})
public class ${EntityEvent.className()} extends ApplicationEvent {

	public static final String CREATED = "CREATED";
	public static final String UPDATED = "UPDATED";
	public static final String DELETED = "DELETED";

	private static final long serialVersionUID = 9152086626754282698L;

	private final String eventType;

	public ${EntityEvent.className()} (String eventType, ${EntityResource.className()} resource) {
		super(resource);
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

}