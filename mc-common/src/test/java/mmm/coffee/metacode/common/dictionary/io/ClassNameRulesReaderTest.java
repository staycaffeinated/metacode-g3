package mmm.coffee.metacode.common.dictionary.io;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class ClassNameRulesReaderTest {

    @Test
    void shouldThrowExceptionWhenResourceLoaderIsNull() {
        assertThrows(NullPointerException.class, () -> new ClassNameRulesReader(null, "hello"));
    }

    @Test
    void shouldThrowExceptionWhenFileNameIsNull() {
        ResourceLoader resourceLoader = new FakeResourceLoader();
        assertThrows(NullPointerException.class, () -> new ClassNameRulesReader(resourceLoader, null));
    }

    @Test
    void shouldReadContent() throws IOException {
        ClassNameRulesReader reader = new ClassNameRulesReader(
                new DefaultResourceLoader(),
                "classpath:/test-classname-rules.properties");
        Map<String, String> map = reader.read();

        assertThat(map).isNotEmpty();
    }

    static class FakeResourceLoader implements ResourceLoader {

        @Override
        public Resource getResource(String location) {
            return new FakeResource();
        }

        @Override
        public ClassLoader getClassLoader() {
            return this.getClass().getClassLoader();
        }
    }

    static class FakeResource implements Resource {

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public URL getURL() throws IOException {
            return new URL("");
        }

        @Override
        public URI getURI() throws IOException {
            return URI.create("");
        }

        @Override
        public File getFile() throws IOException {
            return new File("");
        }

        @Override
        public long contentLength() throws IOException {
            return 0;
        }

        @Override
        public long lastModified() throws IOException {
            return 0;
        }

        @Override
        public Resource createRelative(String relativePath) throws IOException {
            return new FakeResource();
        }

        @Override
        public String getFilename() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream("");
        }
    }
}
