package mmm.coffee.metacode.cli.validation;

import org.junit.jupiter.params.ParameterizedTest;
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
            "123.456.example"
    })
    void shouldNotAcceptInvalidPackageName(String packageName) {
        PackageNameValidator validator = PackageNameValidator.of(packageName);

        assertThat(validator.isValid()).isFalse();
        assertThat(validator.isInvalid()).isTrue();
    }
}
