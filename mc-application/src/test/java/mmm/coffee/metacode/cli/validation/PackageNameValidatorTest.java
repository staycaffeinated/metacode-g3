package mmm.coffee.metacode.cli.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PackageNameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "org.example",
            "_org.example"
    })
    void shouldAcceptValidPackageName(String packageName) {
        PackageNameValidator validator = PackageNameValidator.of(packageName);
        assertThat(validator.isValid()).isTrue();
        assertThat(validator.isInvalid()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "class.Integer.foobar",
            "123.456.example",
            "ORG.EXAMPLE",
            "ACME.ANVILS",
            ".acme.anvils.stuff"
    })
    @NullAndEmptySource
        // nulls and empty strings are also invalid
    void shouldNotAcceptInvalidPackageName(String packageName) {
        PackageNameValidator validator = PackageNameValidator.of(packageName);

        assertThat(validator.isValid()).isFalse();
        assertThat(validator.isInvalid()).isTrue();
        assertThat(validator.errorMessage()).isNotBlank();
    }

    @ParameterizedTest
    @ValueSource(chars = {'0', '1', '9'})
    void shouldRecognizeDigits(char character) {
        FakePackageNameValidator validator = new FakePackageNameValidator(String.valueOf(character));

        assertThat(validator.checkForDigit(character)).isTrue();

        assertThat(validator.checkForUpperCaseLetter(character)).isFalse();
        assertThat(validator.checkForLowerCaseLetter(character)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(chars = {'A', 'B', 'C'})
    void shouldRecognizeUpperCaseLetters(char character) {
        FakePackageNameValidator validator = new FakePackageNameValidator(String.valueOf(character));

        assertThat(validator.checkForUpperCaseLetter(character)).isTrue();

        assertThat(validator.checkForLowerCaseLetter(character)).isFalse();
        assertThat(validator.checkForDigit(character)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'b', 'z'})
    void shouldRecognizeLowerCaseLetters(char character) {
        FakePackageNameValidator validator = new FakePackageNameValidator(String.valueOf(character));

        assertThat(validator.checkForLowerCaseLetter(character)).isTrue();
        assertThat(validator.checkForUpperCaseLetter(character)).isFalse();
        assertThat(validator.checkForDigit(character)).isFalse();
    }


    /*
     * A bit of white-box testing to verify the isDigit/isLowerCaseLetter/isUpperCaseLetter
     * are being exercised.
     */
    class FakePackageNameValidator extends PackageNameValidator {
        public FakePackageNameValidator(String packageName) {
            super(packageName);
        }

        public boolean checkForDigit(char ch) {
            return isDigit(ch);
        }

        public boolean checkForLowerCaseLetter(char ch) {
            return isLowerCaseLetter(ch);
        }

        public boolean checkForUpperCaseLetter(char ch) {
            return isUpperCaseLetter(ch);
        }
    }

}
