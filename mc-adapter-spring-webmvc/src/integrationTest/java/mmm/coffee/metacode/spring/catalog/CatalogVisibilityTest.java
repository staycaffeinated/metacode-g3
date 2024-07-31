package mmm.coffee.metacode.spring.catalog;


import com.google.common.truth.Truth;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/**
 * This test tells us if integration tests have visibility to src/main/resources.
 * If they don't, then our integration-test plugin has a bug
 */
class CatalogVisibilityTest {

    ResourceLoader resourceLoader = new DefaultResourceLoader();

    SpringEndpointCatalog catalogUnderTest;

    RestEndpointDescriptor endpointDescriptor;


    @ParameterizedTest
    @ValueSource(strings = {
            "classpath:/spring/catalogs/v3/spring-webmvc-v3.yml",
            "classpath:/spring/catalogs/v3/spring-webmvc-mongodb-v3.yml"
    })
    void canFindResources(String resourcePath) {
        Resource r1 = resourceLoader.getResource(resourcePath);
        assertThat(r1.exists()).isTrue();
    }

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
    void whenBuildingWebMvcWithMongoDb_expect_WebMvcWithMongoDbEntries() {
        RestEndpointDescriptor mockDescriptor = Mockito.mock(RestEndpointDescriptor.class);
        when(mockDescriptor.isWebFlux()).thenReturn(false);
        when(mockDescriptor.isWithMongoDb()).thenReturn(true);

        catalogUnderTest.prepare(mockDescriptor);
        catalogUnderTest.collect();

        Truth.assertThat(catalogUnderTest.collect()).isNotEmpty();
    }

}
