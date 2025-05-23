/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.catalog;

import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.stereotype.Collector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * SpringEndpointCatalogTests
 */
class SpringEndpointCatalogTests {

    static final String BASE_PATH = "/petstore";
    static final String BASE_PKG = "acme.petstore";
    static final String PET_RESOURCE = "Pet";
    static final String PET_PATH = "/pet";

    SpringEndpointCatalog catalogUnderTest;

    RestEndpointDescriptor endpointDescriptor;

    @BeforeEach
    public void setUp() {
        // @formatter:off
        catalogUnderTest = SpringEndpointCatalog.builder()
                .reader(new CatalogFileReader())
                .build();

        endpointDescriptor = endpointBuilder()
                .framework(Framework.SPRING_WEBMVC.name())
                .withMongoDb(true)
                .build();
        // @formatter:on
    }

    @Test
    void shouldPreProcess() {
        var collector = catalogUnderTest.prepare(endpointDescriptor);
        assertThat(collector).isNotNull();
    }

    @Test
    void shouldReadTemplates() {
        var resultSet = catalogUnderTest.prepare(endpointDescriptor).collect();
        assertThat(resultSet).isNotNull();
        assertThat(resultSet.size()).isGreaterThan(0);
    }

    @Test
    void shouldWorkWithAllArgsConstructor() {
        var catalog = new SpringEndpointCatalog(new CatalogFileReader());
        assertThat(catalog.reader).isNotNull();
        assertThat(catalog.prepare(endpointDescriptor).collect().size()).isGreaterThan(0);
    }

    @Test
    void shouldWrapAnyExceptionAsRuntimeApplicationError() {
        // given: a CatalogFileReader that eagerly throws IOExceptions
        var mockReader = Mockito.mock(CatalogFileReader.class);
        when(mockReader.readCatalog(anyString())).thenThrow(RuntimeApplicationError.class);

        // given: a catalog that uses this iffy reader...
        var catalog = new SpringEndpointCatalog(mockReader);

        // When an IOException occurs when collecting templates,
        // then expect a RuntimeApplicationError is thrown instead
        var collector = catalog.prepare(endpointDescriptor);
        assertThrows(RuntimeApplicationError.class, collector::collect);
    }

    /**
     * Verify the edge case of being handed a Descriptor that's not a RestEndpointDescriptor
     */
    @Test
    void shouldQuietlyIgnoreNonEndpointDescriptor() {
        RestProjectDescriptor mockDescriptor = Mockito.mock(RestProjectDescriptor.class);

        catalogUnderTest.prepare(mockDescriptor);

        // When generating endpoints, only RestEndpointDescriptors can determine the
        // catalogs to load.  With a RestProjectDescriptor, the catalog list will be
        // empty, which means that downstream, there are no templates to render. 
        assertThat(catalogUnderTest.collect()).isEmpty();
    }

    @Nested
    class MongoIntegrationTests {
        @Test
        void whenWebMvcWithMongoIntegration() {
            RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
            when(mockDescriptor.isWebFlux()).thenReturn(false);
            when(mockDescriptor.isWithMongoDb()).thenReturn(false);

            catalogUnderTest.prepare(mockDescriptor);

            assertThat(catalogUnderTest.collect()).isNotEmpty();
        }

        @Test
        void whenWebFlux() {
            RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
            when(mockDescriptor.isWebFlux()).thenReturn(true);
            catalogUnderTest.prepare(mockDescriptor);
            assertThat(catalogUnderTest.collect()).isNotEmpty();
        }
    }

    @Test
    void shouldPrepareWebFluxEndpoint() {
        SpringEndpointCatalog catalog = SpringEndpointCatalog.builder().reader(new CatalogFileReader()).build();
        Collector collector = catalog.prepare(webFluxEndpointDescriptor());
        assertThat(collector).isNotNull();
        assertThat(collector.collect().size()).isGreaterThan(0);

        List<CatalogEntry> items = catalog.collect();
        assertThat(items).isNotNull();
        assertThat(items.size()).isGreaterThan(0);
    }

    @Test
    void shouldPrepareWebMvcEndpoint() {
        SpringEndpointCatalog catalog = SpringEndpointCatalog.builder().reader(new CatalogFileReader()).build();
        Collector collector = catalog.prepare(webMvcEndpointDescriptor());
        assertThat(collector).isNotNull();
        assertThat(collector.collect().size()).isGreaterThan(0);

        List<CatalogEntry> items = catalog.collect();
        assertThat(items).isNotNull();
        assertThat(items.size()).isGreaterThan(0);
    }

    /* -------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------- */

    RestEndpointDescriptor webFluxEndpointDescriptor() {
        return endpointBuilder()
                .framework(Framework.SPRING_WEBFLUX.frameworkName())
                .build();
    }

    RestEndpointDescriptor webMvcEndpointDescriptor() {
        return endpointBuilder()
                .framework(Framework.SPRING_WEBMVC.frameworkName())
                .build();
    }

    RestEndpointDescriptor.RestEndpointDescriptorBuilder endpointBuilder() {
        return RestEndpointDescriptor.builder()
                .basePath(BASE_PATH)
                .basePackage(BASE_PKG)
                .resource(PET_RESOURCE)
                .route(PET_RESOURCE);
    }

}
