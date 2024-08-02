package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.spring.catalog.SpringTemplateCatalog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

class GenSpringComponent_CatalogFileReaderIntegrationTest {

    @Test
    void shouldReadCatalogFile() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        TemplateCatalog catalog = catalogFileReader.readCatalog(SpringTemplateCatalog.WEBMVC_CATALOG);

        assertThat(catalog).isNotNull();
        assertThat(catalog.getEntries()).isNotEmpty();
    }

    @Test
    void shouldReadCatalogFileEntries() throws IOException {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        List<CatalogEntry> entries = catalogFileReader.readCatalog(SpringTemplateCatalog.WEBMVC_MONGODB_CATALOG).getEntries();

        assertThat(entries).isNotNull();
        assertThat(entries).isNotEmpty();
    }
}
