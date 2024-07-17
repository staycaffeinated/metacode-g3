<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import ${endpoint.basePackage}.domain.${endpoint.entityName};
import ${endpoint.basePackage}.math.SecureRandomSeries;
import ${endpoint.basePackage}.spi.ResourceIdSupplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * unit tests
 */
class ${endpoint.entityName}EventTests {

    final ResourceIdSupplier randomSeries = new SecureRandomSeries();

    @Test
    void shouldReturnEventTypeOfCreated() {
        ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${endpoint.entityName}Event(${EntityEvent.className()}.CREATED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${endpoint.entityName}Event.CREATED);
    }

    @Test
    void shouldReturnEventTypeOfUpdated() {
        ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${EntityEvent.className()}(${EntityEvent.className()}.UPDATED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${EntityEvent.className()}.UPDATED);
    }

    @Test
    void shouldReturnEventTypeOfDeleted() {
        ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${EntityEvent.className()}(${EntityEvent.className()}.DELETED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${EntityEvent.className()}.DELETED);
    }
}