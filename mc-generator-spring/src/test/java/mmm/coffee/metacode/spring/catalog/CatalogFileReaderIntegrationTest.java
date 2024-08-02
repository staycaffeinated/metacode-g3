package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.catalog.TemplateCatalog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

class CatalogFileReaderIntegrationTest {

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
