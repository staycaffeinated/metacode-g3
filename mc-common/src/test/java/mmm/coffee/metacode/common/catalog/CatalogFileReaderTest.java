package mmm.coffee.metacode.common.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Optional;

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
        Optional<TemplateCatalog> maybeCatalog = catalogFileReader.readCatalog(catalogFileName());

        assertThat(maybeCatalog.isPresent()).isTrue();
        assertThat(maybeCatalog.get().getEntries()).isNotEmpty();
    }

    @Disabled("Not finding resource folder")
    void shouldReadCatalogFileEntries() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        Optional<TemplateCatalog> catalog = catalogFileReader.readCatalog(catalogFileName());
        assertThat(catalog.isPresent()).isTrue();
        List<CatalogEntry> entries = catalog.get().getEntries();

        assertThat(entries).isNotNull();
        assertThat(entries).isNotEmpty();
    }

    private String catalogFileName() {
        ResourceLoader ourResourceLoader = new DefaultResourceLoader();
        Resource resource = ourResourceLoader.getResource("classpath:/catalogs/example-catalog.yml");
        assertThat(resource.exists()).isTrue();
        return resource.getFilename();
    }
}

