package mmm.coffee.metacode.cli.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CharactersTest {

    @ParameterizedTest
    @ValueSource(chars = {'a','b','z'})
    void shouldRecognizeLowerCaseLetters(char input) {
        assertThat(Characters.isLowerCase(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(chars = { 'A','B','Z', '0', '9', '$', '_', ' ', '.'})
    void shouldRecognizeNonLowerCaseLetters(char input) {
        assertThat(Characters.isLowerCase(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(chars = {'A','B','Z'})
    void shouldRecognizeUpperCaseLetters(char input) {
        assertThat(Characters.isUpperCase(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(chars = { 'a','b','z', '0', '9', '$', '_', ' ', '.'})
    void shouldRecognizeNonUpperCaseLetters(char input) {
        assertThat(Characters.isUpperCase(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(chars = {'0','1','5','8','9'})
    void shouldRecognizeDigits(char input) {
        assertThat(Characters.isDigit(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(chars = {'_','-',' ','A','Z', '$', '&', '.'})
    void shouldRecognizeNonDigits(char input) {
        assertThat(Characters.isDigit(input)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(chars = {'a','b','z','A','Z', '_', 'M', 'm', '9', '0', '1', '5'})
    void shouldRecognizeLetterDigitAndUnderscore(char input) {
        assertThat(Characters.isLetterOrDigitOrUnderscore(input)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(chars = {'~','.','$',' ','#', '!', '-', '<', '>', ';', '"', '|'})
    void shouldRecognizeNonLetterDigitOrUnderscore(char input) {
        assertThat(Characters.isLetterOrDigitOrUnderscore(input)).isFalse();
    }
}
