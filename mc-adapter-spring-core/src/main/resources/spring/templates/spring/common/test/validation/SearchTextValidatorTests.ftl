<#include "/common/Copyright.ftl">
package ${SearchTextValidator.packageName()};

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;                                  :x
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
* Unit tests of SearchTextValidator
*/
@SuppressWarnings("all")
class SearchTextValidatorTest {
    SearchTextValidator validationUnderTest = new SearchTextValidator();

    @Mock
    ConstraintValidatorContext mockContext;

    @Nested
    class PositiveTestCases {
        @ParameterizedTest
        @ValueSource(strings = {"something", "Something", "SOMETHING", "A", "Abc", "abc" })
        @EmptySource
        void shouldAllowAlphabetic(String candidateText) {
            assertThat(validationUnderTest.isValid(Optional.of(candidateText), mockContext)).isTrue();
        }
    }

    @Nested
    class NegativeTestCases { 
        @ParameterizedTest
        @ValueSource(strings = {
            "supercalifragilisticexpialidocious"  // too long
        })
        void shouldNotAllowInvalidText(String candidateText) {
            assertThat(validationUnderTest.isValid(Optional.of(candidateText), mockContext)).isFalse();
        }

        @Test
        void shouldNotAllowNull() {
            assertThrows(NullPointerException.class, () -> validationUnderTest.isValid(null, mockContext));
        }
    }
}
