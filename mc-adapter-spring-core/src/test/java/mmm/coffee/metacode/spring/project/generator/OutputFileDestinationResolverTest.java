package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.catalog.TemplateFacetBuilder;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.trait.DecodeTrait;
import mmm.coffee.metacode.spring.SpringTemplateModelFixture;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class OutputFileDestinationResolverTest {

    static final String SAMPLE_TEMPLATE = "/some/folder/with/template.ftl";
    static final String MAIN = "main";
    static final String TEST = "test";
    static final String INTEGRATION_TEST = "integrationTest";
    static final String TEST_FIXTURES = "testFixtures";
    static final String BASE_PACKAGE = "org.example.petstore";
    static final String REST_RESOURCE = "Pet";

    DecodeTrait defaultDecoder = MustacheDecoder.builder().build();

    @ParameterizedTest
    @MethodSource("provideFacets")
    void shouldResolveDestinationOfProjectArtifact(TemplateFacet facet) throws IOException {
        String destination = OutputFileDestinationResolver.resolveDestination(
                facet,
                Archetype.Application.toString(),
                aSpringProjectTemplateModel(),
                defaultDecoder);

        assertThat(destination).isNotBlank();
    }

    @ParameterizedTest
    @MethodSource("provideFacets")
    void shouldResolveDestinationOfEndpointArtifact(TemplateFacet facet) throws IOException {
        String destination = OutputFileDestinationResolver.resolveDestination(
                facet,
                Archetype.Controller.toString(),
                aSpringEndpointTemplateModel(),
                defaultDecoder);

        assertThat(destination).isNotBlank();
    }

    @Test
    void whenTemplateHasNullCustomProperties_expectNullDestination() {
        SpringTemplateModel templateModel = Mockito.mock(SpringTemplateModel.class);
        when(templateModel.getCustomProperties()).thenReturn(null);

        String output = OutputFileDestinationResolver.resolveDestination(
                aTemplateFacetOf(MAIN),
                Archetype.ApplicationConfiguration.toString(),
                templateModel,
                defaultDecoder);
        assertThat(output).isNull();
    }

    @Test
    void whenUnknownFacet_expectException() throws IOException {
        SpringTemplateModel templateModel = aSpringProjectTemplateModel();
        TemplateFacet facet = aTemplateFacetOf("UnknownFacet");
        String archetypeName = Archetype.ApplicationConfiguration.toString();

        assertThrows(RuntimeApplicationError.class,
                () -> OutputFileDestinationResolver.resolveDestination(facet, archetypeName, templateModel, defaultDecoder));
    }

    @Test
    void whenNullFacetName_expectException() throws IOException {
        SpringTemplateModel templateModel = aSpringProjectTemplateModel();
        String archetypeName = Archetype.ApplicationConfiguration.toString();

        // Given a facet that has a null name instead of an expected value
        TemplateFacet facet = aTemplateFacetOf("UnknownFacet");
        facet.setFacet(null);

        // expect an NPE
        assertThrows(NullPointerException.class,
                () -> OutputFileDestinationResolver.resolveDestination(facet, archetypeName, templateModel, defaultDecoder));
    }

    /* ----------------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ---------------------------------------------------------------------------------------------------------- */

    private static Stream<Arguments> provideFacets() {
        return Stream.of(
                Arguments.of(aTemplateFacetOf(MAIN)),
                Arguments.of(aTemplateFacetOf(TEST)),
                Arguments.of(aTemplateFacetOf(INTEGRATION_TEST)),
                Arguments.of(aTemplateFacetOf(TEST_FIXTURES)));
    }


    static TemplateFacet aTemplateFacetOf(String facet) {
        return TemplateFacetBuilder.builder().destination("").facet(facet).source(SAMPLE_TEMPLATE).build();

    }

    SpringTemplateModel aSpringProjectTemplateModel() throws IOException {
        return SpringTemplateModelFixture.aSpringEndpointTemplateModel();
    }

    SpringTemplateModel aSpringEndpointTemplateModel() throws IOException {
        return SpringTemplateModelFixture.aSpringEndpointTemplateModel();
    }
}
