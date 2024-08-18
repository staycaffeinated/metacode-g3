<#include "/common/Copyright.ftl">
package ${EntityWithText.packageName()};

import ${Entity.fqcn()};
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
* Unit tests
*/
public class ${EntityWithText.testClass()} {

    private final CriteriaBuilder mockCB = Mockito.mock(CriteriaBuilder.class);
    private final Predicate mockPredicate = Mockito.mock(Predicate.class);

    private final Root<?> mockRoot = Mockito.mock(Root.class);
    private final CriteriaQuery<?> mockQuery = Mockito.mock(CriteriaQuery.class);

    @BeforeEach
    public void mockUpCriteriaBuilder() {
        given(mockCB.isTrue(any())).willReturn(mockPredicate);
        given(mockCB.like(any(), anyString())).willReturn(mockPredicate);
    }

    @Nested
    class ConstructorTests {
        @Test
        void shouldConvertTextToLowerCase() {
            final String text = "SOMETHING";
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}(text);
            assertThat(spec.getText()).contains(text.toLowerCase());
        }

        @Test
        void shouldSupportNullText() {
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}(null);
            assertThat(spec.getText()).isNull();
        }

        @Test
        void shouldSupportEmptyString() {
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}("");
            assertThat(spec.getText()).isEmpty();
        }
    }

    @Nested
    @SuppressWarnings("unchecked")
        /**
        * These tests only confirm branch coverage. Integration tests
        * in the controller verify the returned Predicate provides the correct semantics.
        */
    class ToPredicateTests {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"something", "SOMETHING"})
        void shouldReturnPredicateForAnyText(String source) {
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}(source);
            Predicate actual = spec.toPredicate((Root<${Entity.className()}>) mockRoot, mockQuery, mockCB);
            assertThat(actual).isNotNull();
        }
    }
}