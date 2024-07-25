<#include "/common/Copyright.ftl">

package ${EntityCallback.packageName()};

import ${Entity.fqcn()};
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.relational.core.sql.SqlIdentifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


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
        ${Entity.className()} entity = Mockito.mock(${Entity.className()}.class);
        when(entity.getResourceId()).thenReturn(uid);

        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);
        assertThat(publisher).isNotNull();
        // If the entity does not have an id, beforeInsert() is called to assign one
        verify(entity, times(1)).beforeInsert();
    }

    @Test
    @DisplayName("When the resourceId is already set, the resourceId should not be changed")
    void shouldNotChangeExistingResourceId() {
        ${Entity.className()} entity = Mockito.mock(${Entity.className()}.class);
        when(entity.getResourceId()).thenReturn("QPCJlA4db05DVdACxd1GQZGN1lb"); // a randomly chosen ID.

        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);
        assertThat(publisher).isNotNull();
        // If the entity already has an ID, beforeInsert() must not be called
        verify(entity, times(0)).beforeInsert();
    }
}