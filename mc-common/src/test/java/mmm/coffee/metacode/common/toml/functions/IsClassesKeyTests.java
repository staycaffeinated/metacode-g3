package mmm.coffee.metacode.common.toml.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;

public class IsClassesKeyTests {
    IsDottedKeyForPackages classUnderTest = new IsDottedKeyForPackages();

    @ParameterizedTest
    @ValueSource(strings = {
            "infra.advice.classes",
            "home.classes",
            "infra.utils.classes",
            "_RESOURCE_.spi.database.converter.classes",
            "_RESOURCE_.domain.classes",
            "_RESOURCE_.api.classes",
            "spi.classes",
            "spi.database.classes"
    })
    void shouldRecognizeClassesKey(String candidate) {
        assertThat(classUnderTest.test(candidate)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "_RESOURCE_.api.classNames.Controller",
            "_RESOURCE_.api.classNames.ServiceApi",
            "infra.trait.classNames",
            "Internal.TestClassSuffixes.integrationTest",
            "title",
            "version",
            ""
    })
    @NullAndEmptySource
    void shouldRecognizeNonClassesKey(String candidate) {
        assertThat(classUnderTest.test(candidate)).isFalse();
    }
}
