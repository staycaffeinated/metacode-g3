package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

class SpringBootTemplateCatalogTest {

    @Test
    void shouldCollectEntriesFromItsCatalog() {
        SpringBootTemplateCatalog catalog = new SpringBootTemplateCatalog(new CatalogFileReader());
        assertThat(catalog.collect()).isNotEmpty();
    }
}
