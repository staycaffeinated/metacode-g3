package mmm.coffee.metacode.common.dependency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DependencyFileReaderTest {
    private static final String TEST_FILE = "/dependencies/example-dependencies.properties";

    DependencyFileReader readerUnderTest = new DependencyFileReader();

    @Test
    void shouldReadEntriesFromTheFile() throws Exception {
        Library library = readerUnderTest.readDependencyFile(TEST_FILE);

        assertThat(library).isNotNull();
        assertThat(library.getDependencies().size()).isAtLeast(2);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionIfFileNameIsNull(String location) {
        assertThrows(NullPointerException.class, () -> readerUnderTest.readDependencyFile(location));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionIfLocationIsNull(String location) {
        assertThrows(NullPointerException.class, () -> readerUnderTest.read(location));
    }

}
