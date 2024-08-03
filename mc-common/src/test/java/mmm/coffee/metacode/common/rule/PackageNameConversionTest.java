package mmm.coffee.metacode.common.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackageNameConversionTest {

    @Test
    void shouldThrowNullPointerExceptionWhenArgIsNull() {
        assertThrows (NullPointerException.class, ()-> PackageNameConversion.toPath(null));
    }

    @Test
    void shouldReturnEmptyStringWhenArgIsEmpty() {
        String actual = PackageNameConversion.toPath("");
        assertThat (actual).isEmpty();
    }

    @Test
    void shouldConvertWellFormedPackageNameToPath() {
        String actual = PackageNameConversion.toPath("mmm.coffee.widget");
        assertThat(actual).isEqualTo("mmm/coffee/widget");
    }

    @ParameterizedTest
    @CsvSource({
            "mmm.coffee.widget.Widget, mmm/coffee/widget/Widget.java",
            "org.example.petstore.Application, org/example/petstore/Application.java"
    })
    void shouldConvertFqClassNameToFqFilename(String sample, String expected) {
        String actual = PackageNameConversion.toFqFileName(sample);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhenFqcnIsNull() {
        assertThrows(NullPointerException.class, () -> PackageNameConversion.toFqFileName(null));
    }

}
