package mmm.coffee.metacode.common.dictionary.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.NonNull;
import mmm.coffee.metacode.common.dictionary.PackageLayout;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

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
        if (mapper != null) {
            this.mapper = mapper;
        }
        else {
            this.mapper = new ObjectMapper();
        }
        if (resourceLoader != null) {
            this.resourceLoader = resourceLoader;
        }
        else {
            this.resourceLoader = new DefaultResourceLoader();
        }
    }


    public PackageLayout read(@NonNull String jsonFile) throws IOException {
        InputStream is = resourceLoader.getResource(jsonFile).getInputStream();
        ObjectReader reader = mapper.readerFor(PackageLayout.class);
        PackageLayout packageLayout = reader.readValue(is, PackageLayout.class);
        return packageLayout;
    }

}
