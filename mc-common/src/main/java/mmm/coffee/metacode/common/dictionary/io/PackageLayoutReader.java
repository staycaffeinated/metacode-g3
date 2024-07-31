package mmm.coffee.metacode.common.dictionary.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.NonNull;
import mmm.coffee.metacode.common.dictionary.PackageLayout;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PackageLayoutReader {

    private final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;

    /**
     * Construct this reader with a default ObjectMapper
     */
    public PackageLayoutReader() {
        mapper = new ObjectMapper();
        resourceLoader = new DefaultResourceLoader();
    }

    /**
     * Construct this reader with a preconfigured ObjectMapper and ResourceLoader
     * If the preconfigured ObjectMapper is `null`, a default `ObjectMapper` is used
     * If the preconfigured ResourceLoader is `null`, a `DefaultResourceLoader` is used.
     * @param mapper a preconfigured mapper
     */
    public PackageLayoutReader(ObjectMapper mapper, ResourceLoader resourceLoader) {
        this.mapper = Objects.requireNonNullElseGet(mapper, ObjectMapper::new);
        this.resourceLoader = Objects.requireNonNullElseGet(resourceLoader, DefaultResourceLoader::new);
    }

    /**
     * Reads the given `jsonFile` and materializes the json file as a `PackageLayout`
     * @param jsonFile the resource path of the json file (eg: "classpath:/path/to/file")
     * @return the PackageLayout representation
     * @throws IOException an InputStream cannot be created for the `jsonFile`. 
     */
    public PackageLayout read(@NonNull String jsonFile) throws IOException {
        InputStream is = resourceLoader.getResource(jsonFile).getInputStream();
        ObjectReader reader = mapper.readerFor(PackageLayout.class);
        return reader.readValue(is, PackageLayout.class);
    }

}
