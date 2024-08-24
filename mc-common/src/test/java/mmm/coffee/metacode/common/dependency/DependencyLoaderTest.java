package mmm.coffee.metacode.common.dependency;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyLoaderTest {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final DependencyLoader dependencyLoader = new DependencyLoader(resourceLoader);
    final String location = "dependencies/example-dependencies.properties";


    @Test
    void shouldLoadDependencyList() throws IOException {
        Properties properties = dependencyLoader.read(location);
        Library library = new Library();
        properties.forEach((k, v) -> {
            Dependency dependency = new Dependency((String) k, (String) v);
            library.getDependencies().add(dependency);
        });
        assertThat(library.getDependencies()).hasSize(properties.size());
    }

    @Test
    void shouldLoadLibrary() throws IOException {
        Library library = dependencyLoader.loadLibrary(location);
        assertThat(library).isNotNull();
        // We presume there's at least one dependency injected by the code generator.
        // Since the number of dependencies isn't fixed, we only confirm at least 1 was defined.
        assertThat(library.getDependencies()).hasSizeGreaterThan(0);
    }
}
