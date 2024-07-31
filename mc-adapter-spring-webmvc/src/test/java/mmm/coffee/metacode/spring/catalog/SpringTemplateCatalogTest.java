package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringTemplateCatalogTest {

    ResourceLoader resourceLoader = new DefaultResourceLoader();
    SpringWebMvcTemplateCatalog catalogUnderTest;


    @BeforeEach
    public void setUp() {
        CatalogFileReader catalogFileReader = new CatalogFileReader();
        catalogUnderTest = new SpringWebMvcTemplateCatalog(catalogFileReader);
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "classpath:/spring/catalogs/v3/spring-webmvc-v3.yml",
            "classpath:/spring/catalogs/v3/spring-webmvc-mongodb-v3.yml"
    })
    void canFindResources(String resourcePath) {
        Resource r1 = resourceLoader.getResource(resourcePath);
        assertThat(r1.exists()).isTrue();
    }

    @Test
    void canFindCatalogs() {
        String catalogName = catalogUnderTest.getActiveCatalog();
        Resource r1 = resourceLoader.getResource(catalogName);
        assertThat(r1.exists()).isTrue();
    }

    @Test
    void shouldCollectCatalogEntries() {
        /*
         * The catalog needs to be converted to the new format for this test to pass.
         */
        List<CatalogEntry> resultSet = catalogUnderTest.collect();
        assertThat(resultSet).isNotNull().isNotEmpty();
        for (CatalogEntry entry : resultSet) {
            Archetype val = Archetype.valueOf(entry.getArchetype());
            assertThat(val).isNotNull();
            assertThat(entry.getArchetype()).isNotNull();
            assertThat(entry.getFacets()).isNotEmpty();
            assertThat(entry.getScope()).isNotEmpty();
        }
    }
}
