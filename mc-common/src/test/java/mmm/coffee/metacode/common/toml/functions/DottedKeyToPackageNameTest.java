package mmm.coffee.metacode.common.toml.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;

public class DottedKeyToPackageNameTest {
    DottedKeyToPackageName classUnderTest = new DottedKeyToPackageName();

    @ParameterizedTest
    @CsvSource(value = {
            "infra.validation.classes, infra.validation",
            "spi.classes,spi",
            "spi.database.classes,spi.database",
            "infra.advice.classes,infra.advice",
            "exceptions.classes,exceptions"
    })
    void shouldMapDottedKeyToCorrectPackageName(String dottedKey, String expected) {
        assertThat(classUnderTest.toPackageName(dottedKey)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "infra.utils.classNames.SecureRandomSeries",
            "_RESOURCE_.domain.classNames.ResourcePojo",
            "_RESOURCE_.api.classNames.ServiceImpl",
            "Internal.TestClassSuffixes.unitTest",
            "title",
            "version"
    })
    @NullAndEmptySource
    void shouldMapInvalidDottedKeysToNull(String input) {
        assertThat(classUnderTest.toPackageName(input)).isNull();
    }
}
