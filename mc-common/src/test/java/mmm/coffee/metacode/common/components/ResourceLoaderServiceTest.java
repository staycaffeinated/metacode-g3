package mmm.coffee.metacode.common.components;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
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

    @Test
    void shouldThrowExceptionWhenAttemptingToSetNullResourceLoader() {
        assertThrows(NullPointerException.class, () -> resourceLoaderService.setResourceLoader(null));
    }

    @Test
    void shouldThrowExceptionWhenAttemptingToConstructWithNullResource() {
        assertThrows(NullPointerException.class, () -> new ResourceLoaderService(null));
    }

    @Test
    void shouldThrowExceptionWhenAttemptingToLoadNullResource() {
        assertThrows(NullPointerException.class, () -> resourceLoaderService.loadResourceAsInputStream(null));
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
    void shouldAssumeClassPathResourceAsDefaultProtocol() throws Exception {
        // Declare the path without `classpath:` in front; 
        String resourcePath = "catalogs/example-catalog.yml";
        InputStream is = resourceLoaderService.loadResourceAsInputStream(resourcePath);
        assertThat(is).isNotNull();
    }

}
