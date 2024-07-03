<#include "/common/Copyright.ftl">
package ${Document.packageName()};

import ${EntityResource.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
* Unit tests
*/
@SuppressWarnings("all")
public class ${Document.testClass()} {

    private ${Document.className()} objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = ${DocumentTestFixtures.className()}.sampleOne();
    }

    @Nested
    class EqualsMethod {
        @Test
        void shouldEqualItself() {
            assertThat(objectUnderTest.equals(objectUnderTest)).isTrue();
        }

        @Test
        void shouldNotEqualNull() {
            assertThat(objectUnderTest.equals(null)).isFalse();
        }

        @Test // for code coverage
        void shouldNotBeEqualWhenResourceIdsAreDifferent() {
            // This test merely exercises a branch within the equals method;
            ${Document.className()} other = ${DocumentTestFixtures.className()}.sampleTwo();
            assertThat(objectUnderTest.getResourceId()).isNotEqualTo(other.getResourceId());
            assertThat(objectUnderTest.equals(other)).isFalse();
        }

        @Test
        void shouldNotEqualOtherClasses() {
            ${EntityResource.className()} pojo = ${WebMvcModelTestFixtures.className()}.sampleOne();
            assertThat(objectUnderTest.equals(pojo)).isFalse();
        }

        @Test
        void shouldBeEqual() {
            ${endpoint.documentName} that = ${DocumentTestFixtures.className()}.sampleOne();
            assertThat(objectUnderTest.equals(that)).isTrue();
        }
    }

    @Nested
    class HashCodeMethod {
        @Test
        void shouldComputeHashCode() {
            assertThat(objectUnderTest.hashCode()).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        @Test
        void shouldYieldSameHashCode() {
            final String randomId = "abc12345XYZ";
            ${Document.className()} sampleOne = ${DocumentTestFixtures.className()}.copyOf(${DocumentTestFixtures.className()}.sampleOne());
            sampleOne.setResourceId(randomId);

            ${Document.className()} sampleTwo = ${DocumentTestFixtures.className()}.copyOf(${DocumentTestFixtures.className()}.sampleTwo());
            sampleTwo.setResourceId(randomId);

            assertThat(sampleOne.hashCode()).isEqualTo(sampleTwo.hashCode());
        }
    }
}

