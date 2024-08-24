package mmm.coffee.metacode.common.dependency;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


// Other name suggestion: DependencySourceConnector,
// along with TemplateSinkConnector, TemplateSourceConnector,
// DependencyVersionSupplier (or should Supplier impl the java.util.function.Supplier interface?)
@RequiredArgsConstructor
public class DependencyLoader {

    private final ResourceLoader resourceLoader;

    public Properties read(@NonNull String location) throws IOException {
        Properties properties = new Properties();
        properties.load(resourceLoader.getResource(location).getInputStream());
        return properties;
    }

    public Library loadLibrary(@NonNull String location) throws IOException {
        Properties properties = read(location);
        Library library = new Library();
        library.setDependencies(new ArrayList<>());
        properties.forEach((k, v) -> {
            Dependency dependency = new Dependency((String) k, (String) v);
            library.getDependencies().add(dependency);
        });
        return library;
    }
}
