package mmm.coffee.metacode.common.components;


import lombok.NonNull;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.springframework.util.ResourceUtils.FILE_URL_PREFIX;

@Component("resourceLoaderService")
public class ResourceLoaderService implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    public ResourceLoaderService(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Load either a file resource. The path can be a file protocol (e.g.: "file:/path/to/the/file")
     * or classpath protocol (eg: "classpath:/path/to/file").  If neither of these protocols
     * are used, `classpath:` is the default protocol.
     */
    public InputStream loadResourceAsInputStream(@NonNull String resourcePath) throws IOException {
        if (!pathStartsWithProtocol(resourcePath)) {
            resourcePath = CLASSPATH_URL_PREFIX + resourcePath;
        }
        Resource resource = resourceLoader.getResource(resourcePath);
        if (resource.exists()) {
            return resource.getInputStream();
        }
        throw new FileNotFoundException(resourcePath);
    }

    private boolean pathStartsWithProtocol(String value) {
        if (value.startsWith(FILE_URL_PREFIX)) return true;
        return value.startsWith(CLASSPATH_URL_PREFIX);
    }
}