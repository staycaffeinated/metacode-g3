/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.common.catalog;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.NonNull;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.springframework.util.ResourceUtils.FILE_URL_PREFIX;

/**
 * The {@code CatalogFileReader} reads the catalog files containing
 * the inventory of templates to render.
 * <p>
 * To get from a template to a rendered file, we need to know
 * two things: the location of the template and the destination
 * of the rendered content.  These two pieces of information are
 * captured in a {@code CatalogEntry}.
 * <p>
 * To create an inventory of all the templates to render, we
 * have the notion of "catalogs", where a catalog contains a list
 * of CatalogEntry's. We also support multiple catalogs, since
 * being able to organize CatalogEntry's into different files is useful.
 * <p>
 * Catalogs are stored as YAML files, saved as resource bundles.
 * By convention, the catalog files are saved in a folder named "catalogs".
 * Thus, the classpath to the catalogs is something like "/restapi/catalogs/",
 * which can be found in a module's "src/main/resources" folder.
 * <p>
 * Likewise, the Freemarker templates are TFL files saved as resource bundles,
 * with a classpath something like "/restapi/templates/".
 * <p>
 * From the point of view of a CodeGenerator, the generator needs the (primary)
 * catalog of templates to process. (Some filtering is supported since a code
 * generator can exclude templates not to render, based on command-line options.)
 * <p>
 * Side note: An idea on adding Keywords attribute to catalog entries:
 * <a href="https://github.com/helm/helm/issues/7771">helm search by keywords</a>
 */
@Component
public class CatalogFileReader implements ICatalogReader {

    private final ResourceLoader resourceLoader;

    /**
     * Default constructor
     */
    public CatalogFileReader() {
        resourceLoader = new DefaultResourceLoader();
    }

    /**
     * Constructor with a preferred `resourceLoader`.
     */
    @Autowired
    public CatalogFileReader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /*
     * Reads the given catalog file, returning the content
     * as CatalogEntry's.  The {@code catalogClassPath} will
     * look something like "/spring/catalogs/spring-boot.yml"
     * or "/spring/catalogs/spring-gradle.yml".
     *
     * @param catalogClassPath the resource path of the catalog.yaml file
     * @return the entries of the catalog, as a list of CatalogEntry
     */
    public Optional<TemplateCatalog> readCatalog(@NonNull String catalogLocation) {
        ObjectMapper objectMapper = getYamlFriendlyObjectMapper();
        try (InputStream is = loadResourceAsInputStream(catalogLocation)) {
            // fail fast if file isn't found
            Objects.requireNonNull(is, String.format("The catalog file, '%s', was not found. Verify the resource exists at the given path.", catalogLocation));
            return Optional.of(objectMapper.readValue(is, TemplateCatalog.class));
        } catch (IOException e) {
            String msg = String.format("Unable to read catalog file: %s", catalogLocation);
            throw new RuntimeApplicationError(msg, e);
        }
    }

    private ObjectMapper getYamlFriendlyObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

        /*
         * This can also be set at class-level with an annotation: @JsonIgnoreProperties(ignoreUnknown = true)
         */
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        return mapper;
    }

    /**
     * Load either a file resource. The path can be a file protocol (e.g.: "file:/path/to/the/file")
     * or classpath protocol (eg: "classpath:/path/to/file").  If neither of these protocols
     * are used, `classpath:` is the default protocol.
     */
    private InputStream loadResourceAsInputStream(@NonNull String resourcePath) throws IOException {
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
