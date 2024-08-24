package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.spring.catalog.SpringTemplateCatalog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

class GenSpringComponent_CatalogFileReaderIntegrationTest {

    @Test
    void shouldReadCatalogFile() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        Optional<TemplateCatalog> catalog = catalogFileReader.readCatalog(SpringTemplateCatalog.WEBMVC_CATALOG);

        assertThat(catalog).isPresent();
        assertThat(catalog.get().getEntries()).isNotEmpty();
    }

    @Test
    void shouldReadCatalogFileEntries() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        Optional<TemplateCatalog> catalog = catalogFileReader.readCatalog(SpringTemplateCatalog.WEBMVC_MONGODB_CATALOG);
        List<CatalogEntry> entries = catalog.isPresent() ? catalog.get().getEntries() : List.of();

        assertThat(entries).isNotNull();
        assertThat(entries).isNotEmpty();
    }
}
