/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import mmm.coffee.metacode.common.catalog.TemplateCatalog;
import mmm.coffee.metacode.common.components.ResourceLoaderService;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * SpringTemplateCatalogTests
 */
class SpringTemplateCatalogTests {

    /*
     * These are the only two catalogs visible within the test scope of the adapter-spring-core component.
     */
    private static final String COMMON_STUFF = "/spring/catalogs/v2/common-stuff.yml";
    private static final String SPRING_GRADLE = "/spring/catalogs/v2/spring-gradle.yml";

    ResourceLoader resourceLoader = new DefaultResourceLoader();

    @ParameterizedTest
    @ValueSource(strings = {COMMON_STUFF, SPRING_GRADLE})
    void shouldReadCatalog(String catalogName) throws IOException {
        CatalogFileReader reader = new CatalogFileReader(resourceLoader);
        TemplateCatalog catalog= reader.readCatalog(catalogName);

        assertThat(catalog).isNotNull();
        assertThat(catalog.getEntries()).isNotEmpty();
    }

    @Test
    void shouldThrowException() throws Exception {
        var mockReader = Mockito.mock(ICatalogReader.class);
        when(mockReader.readCatalog(anyString())).thenThrow(IOException.class);

        SimpleTemplateCatalog catalog = new SimpleTemplateCatalog(mockReader);
        assertThrows(RuntimeApplicationError.class, () -> catalog.collectGeneralCatalogsAndThisOne(""));
    }

    // -----------------------------------------------------------
    // Helper methods and classes
    // -----------------------------------------------------------
    static class SimpleTemplateCatalog extends SpringTemplateCatalog {

        SimpleTemplateCatalog(ICatalogReader reader) {
            super(reader);
        }

        @Override
        public List<CatalogEntry> collect() {
            return Collections.emptyList();
        }
    }
}
