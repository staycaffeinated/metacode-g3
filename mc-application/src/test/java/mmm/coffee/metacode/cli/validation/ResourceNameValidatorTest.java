package mmm.coffee.metacode.cli.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNameValidatorTest {
    @ParameterizedTest
    @ValueSource(strings = {"Employee", "employee", "Test_Suite", "xyzzy", "_foobar", "$foo"})
    void shouldRecognizeValidIdentifiers(String identifier) {
        assertThat(ResourceNameValidator.of(identifier).isValid()).isTrue();
        assertThat(ResourceNameValidator.of(identifier).isInvalid()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abstract", "null", "const", "float", "'Hello'", "/Hello"})
    void shouldRecognizeInvalidIdentifiers(String identifier) {
        assertThat(ResourceNameValidator.of(identifier).isValid()).isFalse();
        assertThat(ResourceNameValidator.of(identifier).isInvalid()).isTrue();
    }

    /**
     * Some class names are not allowed because they lead to compile-time errors.
     * Case in point, a class named 'Test' will end up conflicting with JUnit's 'Test' class
     * and the generated code will not compile due to the name conflict.  A class named
     * 'User' typically causes runtime errors due to Hibernate/H2 conflicts.
     */
    @ParameterizedTest
    @ValueSource(strings = {"test", "Test"})
    void shouldRecognizeDisallowedWords(String proposedResourceName) {
        assertThat(ResourceNameValidator.of(proposedResourceName).isValid()).isFalse();
    }


    @Test
    void shouldRecognizeReservedWord() {
        assertThat(ResourceNameValidator.of("static").isValid()).isFalse();
    }

    @Test
    void shouldRecognizeUnreservedWord() {
        assertThat(ResourceNameValidator.of("widget").isValid()).isTrue();
    }

    @Test
    void shouldRejectNullValues() {
        assertThat(ResourceNameValidator.of(null).isValid()).isFalse();
    }

    @Test
    void shouldRejectEmptyString() {
        assertThat(ResourceNameValidator.of("").isValid()).isFalse();
    }

    @Test
    void shouldRejectInvalidClassNames() {
        assertThat(ResourceNameValidator.of("Treasure#!Map").isValid()).isFalse();
    }

    @Nested
    class ErrorMessageTests {
        @Test
        void shouldHaveEmptyErrorMessageWhenResourceNameIsValid() {
            ResourceNameValidator validator = ResourceNameValidator.of("Pet");
            assertThat(validator.isValid()).isTrue();
            assertThat(validator.errorMessage()).isEmpty();
        }

        @Test
        void shouldHaveNonEmptyErrorMessageWhenResourceNameIsInvalid() {
            ResourceNameValidator validator = ResourceNameValidator.of("Pet#!Map");
            assertThat(validator.isValid()).isFalse();
            assertThat(validator.errorMessage()).isNotEmpty();
        }
    }
}
