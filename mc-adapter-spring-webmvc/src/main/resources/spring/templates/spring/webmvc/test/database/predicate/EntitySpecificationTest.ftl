<#include "/common/Copyright.ftl">
package ${EntitySpecification.packageName()};

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
import jakarta.persistence.criteria.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests
 */
@SuppressWarnings("unchecked")
class ${EntitySpecification.testClass()} {

    private final CriteriaBuilder mockCB = Mockito.mock(CriteriaBuilder.class);
    private final CriteriaQuery<?> mockQuery = Mockito.mock(CriteriaQuery.class);
    private final Predicate mockPredicate = Mockito.mock(Predicate.class);
    private final Root<${Entity.className()}> mockRoot = Mockito.mock(Root.class);

    @BeforeEach
    void mockUpCriteriaBuilder() {
        given(mockCB.isTrue(any())).willReturn(mockPredicate);
        given(mockCB.like(any(), anyString())).willReturn(mockPredicate);
        given(mockCB.and(any(Predicate.class))).willReturn(mockPredicate);
        given(mockCB.equal(any(Path.class), any(Object.class))).willReturn(mockPredicate);
    }

    @Test
    void verifyCodeCoverageOfTheToPredicateMethod() {
        ${EntitySpecification.className()} spec = aSampleSpecification();
        Predicate predicate = spec.toPredicate(mockRoot, mockQuery, mockCB);

        /* Since mock objects are being used, a real Predicate is not instantiated.
         * The only goal of this test is to verify line coverage by walking the toPredicate method.
         * Even with the ${EntitySpecification.className()} being used in integration tests to
         * query the database, the jacoco sensors do not pick up the coverage of the toPredicate method.
         * Feel free to tweak your Sonar configuration to ignore coverage of the ${EntitySpecification.className()} class.
         */
        assertThat(predicate).isNull();
    }

    @Test
    void shouldThrowExceptionWhenRootArgumentIsNull() {
        ${EntitySpecification.className()} spec = aSampleSpecification();
        assertThrows(NullPointerException.class, () -> spec.toPredicate(null, mockQuery, mockCB));
    }

    @Test
    void shouldThrowExceptionWhenCriteriaBuilderArgumentIsEmpty() {
        ${EntitySpecification.className()} spec = aSampleSpecification();
        assertThrows(NullPointerException.class, () -> spec.toPredicate(mockRoot, mockQuery, null));
    }

    /* -----------------------------------------------------------------------------
     * HELPER METHODS
     * ----------------------------------------------------------------------------- */
    private ${EntitySpecification.className()} aSampleSpecification() {
        return ${EntitySpecification.className()}.builder().resourceId("12345").text("abc").build();
    }

}