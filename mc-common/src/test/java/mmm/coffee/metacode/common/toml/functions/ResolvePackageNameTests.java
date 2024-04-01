package mmm.coffee.metacode.common.toml.functions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

public class ResolvePackageNameTests {
    PackageNameResolver classUnderTest = new PackageNameResolver();

    @ParameterizedTest
    @CsvSource(value = {
            "org.example.petstore._RESOURCE_.api, org.example.petstore.pet.api",
            "com.acme.bookstore._RESOURCE_.spi, com.acme.bookstore.pet.spi"
    })
    void shouldResolvePackageName(String template, String expected) {
        String actualPackageName = classUnderTest.apply("Pet", template);
        assertThat(actualPackageName).isEqualTo(expected);

        String altPackageName = PackageNameResolver.resolve("Pet", template);
        assertThat(altPackageName).isEqualTo(expected);
    }
}
