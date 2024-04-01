package mmm.coffee.metacode.common.toml.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

public class ResolveCanonicalClassNameTests {
    CanonicalClassNameResolver classUnderTest = new CanonicalClassNameResolver();

    @ParameterizedTest
    @CsvSource(value = {
            "Pet,           org.example.domain, org.example.domain.Pet",
            "pet,           org.example.domain, org.example.domain.Pet",
            "HTML_Table,    org.example.domain, org.example.domain.HTML_Table"
    })
    void shouldResolveToFullyQualifiedClassName(String resource, String tokenizedPackage, String expected) {
        String actual = classUnderTest.apply(resource, tokenizedPackage);
        assertThat(actual).isEqualTo(expected);

        String otherActual = CanonicalClassNameResolver.resolve(resource, tokenizedPackage);
        assertThat(otherActual).isEqualTo(expected);
    }
}
