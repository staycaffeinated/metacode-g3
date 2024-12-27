package mmm.coffee.metacode.common.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ResourceLoaderService.class)
class ResourceLoaderServiceTest {

    @Autowired
    ResourceLoaderService resourceLoaderService;

    @Test
    void shouldFindResources() throws Exception {
        InputStream is = resourceLoaderService.loadResourceAsInputStream("classpath:catalogs/example-catalog.yml");
        assertThat(is).isNotNull();
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenAttemptingToSetNullResourceLoader(ResourceLoader source) {
        assertThrows(NullPointerException.class, () -> resourceLoaderService.setResourceLoader(source));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenAttemptingToConstructWithNullResource(ResourceLoader source) {
        assertThrows(NullPointerException.class, () -> new ResourceLoaderService(source));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenAttemptingToLoadNullResource(String resourcePath) {
        assertThrows(NullPointerException.class, () -> resourceLoaderService.loadResourceAsInputStream(resourcePath));
    }

    @Test
    void shouldThrowExceptionIfResourceNotFound() {
        assertThrows(FileNotFoundException.class, () -> resourceLoaderService.loadResourceAsInputStream("nonexistent"));
    }

    @Test
    void shouldHandleFileProtocol(@TempDir Path tempDir) throws Exception {
        Path sample = Files.createFile(tempDir.resolve("some-file.txt"));
        URL url = sample.toUri().toURL();
        InputStream is = resourceLoaderService.loadResourceAsInputStream(url.toString());
        assertThat(is).isNotNull();
    }

    @Test
    void shouldHandleClassProtocol() throws Exception {
        String resourcePath = "classpath:catalogs/example-catalog.yml";
        InputStream is = resourceLoaderService.loadResourceAsInputStream(resourcePath);
        assertThat(is).isNotNull();
    }

    @Test
    @SuppressWarnings("java:S125") // false positive; the one comment is not commented-out code
    void shouldAssumeClassPathResourceAsDefaultProtocol() throws Exception {
        // Declare the path without `classpath:` in front; 
        String resourcePath = "catalogs/example-catalog.yml";
        InputStream is = resourceLoaderService.loadResourceAsInputStream(resourcePath);
        assertThat(is).isNotNull();
    }

}
