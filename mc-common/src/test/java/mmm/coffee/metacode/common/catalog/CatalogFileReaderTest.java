package mmm.coffee.metacode.common.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

class CatalogFileReaderTest {

    ResourceLoader resourceLoader = new DefaultResourceLoader();

    @ParameterizedTest
    @ValueSource(strings = {
            "classpath:/example-templates/service-api-template.ftl",
            "classpath:/example-templates/controller-template.ftl"
    })
    void verifyResourcesAreFound(String resource) {
        Resource r = resourceLoader.getResource(resource);
        assertThat(r).isNotNull();
        assertThat(r.exists()).isTrue();
    }

    @Disabled("Not finding resource folder")
    void shouldBuildDefaultFileReader() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        TemplateCatalog catalog = catalogFileReader.readCatalog(catalogFileName());

        assertThat(catalog).isNotNull();
        assertThat(catalog.getEntries()).isNotEmpty();
    }

    @Disabled("Not finding resource folder")
    void shouldReadCatalogFileEntries() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        List<CatalogEntry> entries = catalogFileReader.readCatalog(catalogFileName()).getEntries();

        assertThat(entries).isNotNull();
        assertThat(entries).isNotEmpty();
    }

    private String catalogFileName() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/catalogs/example-catalog.yml");
        assertThat(resource.exists()).isTrue();
        return resource.getFilename();
    }
}

