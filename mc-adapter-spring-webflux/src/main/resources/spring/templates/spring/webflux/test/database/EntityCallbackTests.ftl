<#include "/common/Copyright.ftl">

package ${EntityCallback.packageName()};

import ${Entity.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.relational.core.sql.SqlIdentifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ${EntityCallback.testClass()} {

    @Mock
    ${ResourceIdSupplier.className()} resourceIdSupplier;

    @InjectMocks
    ${EntityCallback.className()} callbackUnderTest;

    @Mock
    OutboundRow outboundRow;

    @Mock
    SqlIdentifier table;

    @ParameterizedTest
    @DisplayName("When the resourceId is not set, a resourceId should automatically be assigned")
    @NullAndEmptySource()
    void shouldAssignResourceIdWhenNew(String uid) {
        // Given
        ${Entity.className()} entity = Mockito.mock(${Entity.className()}.class);
        when(entity.getResourceId()).thenReturn(uid);
        when(resourceIdSupplier.nextResourceId()).thenReturn("generatedId");

        // When
        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);

        // Then
        assertThat(publisher).isNotNull();
        verify(entity, times(1)).setResourceId("generatedId");
    }

    @Test
    @DisplayName("When the resourceId is already set, the resourceId should not be changed")
    void shouldNotChangeExistingResourceId() {
        // Given
        ${Entity.className()} entity = Mockito.mock(${Entity.className()}.class);
        when(entity.getResourceId()).thenReturn("QPCJlA4db05DVdACxd1GQZGN1lb");

        // When
        Publisher<${Entity.className()}> publisher = callbackUnderTest.onBeforeSave(entity, outboundRow, table);

        // Then
        assertThat(publisher).isNotNull();
        verify(entity, never()).setResourceId(any());
        verify(resourceIdSupplier, never()).nextResourceId();
    }
}