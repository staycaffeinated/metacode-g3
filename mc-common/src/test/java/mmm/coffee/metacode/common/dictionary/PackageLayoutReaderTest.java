package mmm.coffee.metacode.common.dictionary;

import com.fasterxml.jackson.databind.ObjectMapper;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

public class PackageLayoutReaderTest {

    private static final String SAMPLE_FILE = "classpath:test-package-layout.json";

    PackageLayoutReader readerUnderTest = new PackageLayoutReader(new ObjectMapper(), new DefaultResourceLoader());

    @Test
    void shouldReadPackageLayout() throws IOException {
        PackageLayout layout = readerUnderTest.read(SAMPLE_FILE);

        assertThat(layout).isNotNull();
        assertThat(layout.getEntries()).isNotEmpty();

        for (PackageLayoutEntry entry : layout.getEntries()) {
            System.out.println(entry);
        }
    }

    @Test
    void shouldThrowExceptionIfFileNameIsNull() {
        assertThrows(NullPointerException.class, () -> readerUnderTest.read(null));
    }

    @Test
    void whenMapperIsNull_shouldUseDefaultOne() throws Exception {
        PackageLayoutReader reader = new PackageLayoutReader(null, new DefaultResourceLoader());
        PackageLayout layout = reader.read(SAMPLE_FILE);
        assertThat(layout).isNotNull();
        assertThat(layout.getEntries()).isNotEmpty();
    }

    @Test
    void whenResourceLoaderIsNull_shouldUseDefaultOne() throws Exception {
        PackageLayoutReader reader = new PackageLayoutReader(new ObjectMapper(), null);
        PackageLayout layout = reader.read(SAMPLE_FILE);
        assertThat(layout).isNotNull();
        assertThat(layout.getEntries()).isNotEmpty();
    }

}
