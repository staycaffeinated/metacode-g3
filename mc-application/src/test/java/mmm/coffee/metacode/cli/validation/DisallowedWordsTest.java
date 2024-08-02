package mmm.coffee.metacode.cli.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DisallowedWordsTest {
    @ParameterizedTest
    @ValueSource( strings = { "Test", "test", "TEST" })
    void shouldRecognizeDisallowedWords(String proposedResourceName) {
        assertThat(DisallowedWords.isDisallowedWord(proposedResourceName)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( strings = { "Pet", "book", "FOO" })
    void shouldRecognizeAllowedWords(String proposedResourceName) {
        assertThat(DisallowedWords.isDisallowedWord(proposedResourceName)).isFalse();
    }

}
