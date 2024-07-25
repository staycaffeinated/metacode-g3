<#include "/common/Copyright.ftl">

package ${EntityCallback.packageName()};

import ${Entity.fqcn()};
import ${EjbTestFixtures.fqcn()};
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.relational.core.sql.SqlIdentifier;

import static org.assertj.core.api.Assertions.assertThat;

class ${EntityCallback.testClass()} {

    private final ${EntityCallback.className()} callbackUnderTest = new ${EntityCallback.className()}();

    @Mock
    OutboundRow outboundRow;

    @Mock
    SqlIdentifier table;

    @ParameterizedTest
    @DisplayName("When the resourceId is not set, a resourceId should automatically be assigned")
    @NullAndEmptySource()
    void shouldAssignResourceIdWhenNew(String uid) {
        ${Entity.className()} entity = ${EjbTestFixtures.className()}.oneWithoutResourceId();
        entity.setResourceId(uid);
        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);
        assertThat(publisher).isNotNull();
    }

    @Test
    @DisplayName("When the resourceId is already set, the resourceId should not be changed")
    void shouldNotChangeExistingResourceId() {
        ${Entity.className()} entity = ${EjbTestFixtures.className()}.oneWithResourceId();
        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);
        assertThat(publisher).isNotNull();
    }
}