<#include "/common/Copyright.ftl">
package ${AlphabeticValidator.packageName()};

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests of AlphabeticValidation
 */
@ExtendWith(MockitoExtension.class)
class AlphabeticValidatorTest {

    AlphabeticValidator validationUnderTest = new AlphabeticValidator();

    @Mock
    ConstraintValidatorContext mockContext;

    @Nested
    class PositiveTestCases {
        @Test
        void shouldAllowAlphabetic() {
            assertThat(validationUnderTest.isValid("SampleValue", mockContext)).isTrue();
        }
    }

    @Nested
    class NegativeTestCases {

        @Test void shouldNotAllowNull() {
            assertThat ( validationUnderTest.isValid(null, mockContext)).isFalse();
        }

        @Test void shouldNotAllowAlphanumeric() {
            assertThat(validationUnderTest.isValid("abc123DEF", mockContext)).isFalse();
        }

        @Test
        void shouldNotAllowStringWithSpaces() {
            assertThat(validationUnderTest.isValid("Hello World", mockContext)).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc123DEF", "abc-123-def-xyzzy", "abc_123_def_xyzzy", "12345", "!@#$%"})
        void shouldNotAllowNonAlphabeticStrings(String input) {
            assertThat(validationUnderTest.isValid(input, mockContext)).isFalse();
        }
    }

    @Nested
    class EdgeCases {
        @Test
        void shouldHandleUnicodeLetters() {
            // Document whether Unicode alphabetic chars are supported
            // Adjust assertion based on expected behavior
            assertThat(validationUnderTest.isValid("Ñoño", mockContext)).isTrue(); // or isFalse() depending on contract
        }
    }
}
