/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.catalog;

import com.google.common.truth.Truth;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * SpringWebMvcEndpointCatalogTests
 */
class SpringWebMvcEndpointCatalogIT {

    SpringEndpointCatalog catalogUnderTest;

    RestEndpointDescriptor endpointDescriptor;

    @BeforeEach
    public void setUp() {
        // @formatter:off
        catalogUnderTest = SpringEndpointCatalog.builder()
                .reader(new CatalogFileReader())
                .build();

        endpointDescriptor = RestEndpointDescriptor.builder()
                .basePath("/petstore")
                .basePackage("acme.petstore")
                .framework(Framework.SPRING_WEBMVC.name())
                .resource("Pet")
                .route("/pets")
                .withMongoDb(true)
                .build();
        // @formatter:on
    }

    @Test
    void shouldPreProcess() {
        var collector = catalogUnderTest.prepare(endpointDescriptor);
        Truth.assertThat(collector).isNotNull();
    }

    @Test
    void shouldReadTemplates() {
        var resultSet = catalogUnderTest.prepare(endpointDescriptor).collect();
        Truth.assertThat(resultSet).isNotNull();
        Truth.assertThat(resultSet.size()).isGreaterThan(0);
    }

    @Test
    void shouldWorkWithAllArgsConstructor() {
        var catalog = new SpringEndpointCatalog(new CatalogFileReader());
        assertThat(catalog.reader).isNotNull();
        assertThat(catalog.prepare(endpointDescriptor).collect().size()).isGreaterThan(0);
    }

    @Test
    void shouldWrapAnyExceptionAsRuntimeApplicationError() throws Exception {
        // given: a CatalogFileReader that eagerly throws IOExceptions
        var mockReader = Mockito.mock(CatalogFileReader.class);
        when(mockReader.readCatalog(anyString())).thenThrow(IOException.class);

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
        Truth.assertThat(catalogUnderTest.collect()).isEmpty();
    }

    @Nested
    class MongoIntegrationTests {
        @Test
        void whenBuildingWebMvcWithMongoDb_expect_WebMvcWithMongoDbEntries() {
            RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
            when(mockDescriptor.isWebFlux()).thenReturn(false);
            when(mockDescriptor.isWithMongoDb()).thenReturn(true);

            catalogUnderTest.prepare(mockDescriptor);
            List<CatalogEntry> entries = catalogUnderTest.collect();

            Truth.assertThat(catalogUnderTest.collect()).isNotEmpty();
        }

        @Disabled("Need to put this in SpringWebFluxEndpointCatalogTest")
        void whenNotWebFluxAndNotMongoDbIntegration() {
            RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
            when(mockDescriptor.isWebFlux()).thenReturn(false);
            when(mockDescriptor.isWithMongoDb()).thenReturn(false);

            catalogUnderTest.prepare(mockDescriptor);

            Truth.assertThat(catalogUnderTest.collect()).isNotEmpty();
        }

        @Disabled("Need to put this in SpringWebFluxEndpointCatalogTest")
        void whenWebFlux() {
            RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
            when(mockDescriptor.isWebFlux()).thenReturn(true);
            catalogUnderTest.prepare(mockDescriptor);
            Truth.assertThat(catalogUnderTest.collect()).isNotEmpty();
        }
    }
}