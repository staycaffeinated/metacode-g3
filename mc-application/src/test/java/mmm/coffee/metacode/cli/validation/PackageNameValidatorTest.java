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
    @ValueSource(strings = {
            "abc.def.ghi",
            "acme",
            "acme.anvil_tools.coyote"
    })
    void shouldAcceptValidPackageNames(String candidate) {
        FakePackageNameValidator validator = new FakePackageNameValidator(candidate);
        assertThat(validator.evaluate(candidate)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Acme.Tools.Anvils",
            "acme-tools-anvils",
            "9wednesdayDrive.wicked.Spells",
            "int.class.float.byte.boolean.data",
            ".time.of.day.functions",
            "foo.#@!bar.helpers"
    })
    void shouldRejectInvalidPackageNames(String candidate) {
        FakePackageNameValidator validator = new FakePackageNameValidator(candidate);
        assertThat(validator.evaluate(candidate)).isFalse();
    }


    /* ------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------------------ */

    /*
     * A bit of white-box testing to verify the isDigit/isLowerCaseLetter/isUpperCaseLetter
     * are being exercised.
     */
    class FakePackageNameValidator extends PackageNameValidator {
        public FakePackageNameValidator() {
            super("");
        }

        public FakePackageNameValidator(String packageName) {
            super(packageName);
        }

        public boolean checkForLowerCaseLetter(char ch) {
            return Characters.isLowerCase(ch);
        }

        public boolean checkForUpperCaseLetter(char ch) {
            return Characters.isUpperCase(ch);
        }
    }

}
