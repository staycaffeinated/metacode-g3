<#include "/common/Copyright.ftl">
package ${EntityEvent.packageName()};

import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * unit tests
 */
class ${EntityEvent.testClass()} {

    final ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @Test
    void shouldReturnEventTypeOfCreated() {
        ${EntityResource.className()} resource = ${EntityResource.className()}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${EntityEvent.className()}(${EntityEvent.className()}.CREATED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${EntityEvent.className()}.CREATED);
    }

    @Test
    void shouldReturnEventTypeOfUpdated() {
        ${EntityResource.className()} resource = ${EntityResource.className()}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${EntityEvent.className()}(${EntityEvent.className()}.UPDATED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${EntityEvent.className()}.UPDATED);
    }

    @Test
    void shouldReturnEventTypeOfDeleted() {
        ${EntityResource.className()} resource = ${EntityResource.className()}.builder().resourceId(randomSeries.nextResourceId()).text("Hello world").build();
        ${EntityEvent.className()} event = new ${EntityEvent.className()}(${EntityEvent.className()}.DELETED, resource);

   	    assertThat(event.getEventType()).isEqualTo(${EntityEvent.className()}.DELETED);
    }
}