package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.catalog.TemplateFacetBuilder;
import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutToHashMapMapper;
import mmm.coffee.metacode.common.dictionary.io.ClassNameRulesReader;
import mmm.coffee.metacode.common.dictionary.io.PackageLayoutReader;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.trait.DecodeTrait;
import mmm.coffee.metacode.spring.endpoint.model.RestEndpointTemplateModel;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputFileDestinationResolverTest {

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
        return RestProjectTemplateModel.builder()
                .applicationName("petstore")
                .basePackage(BASE_PACKAGE)
                .basePath("/petstore")
                .groupId("org.acme")
                .isWebMvc(true)
                .customProperties(projectScopeCustomProperties())
                .build();
    }

    SpringTemplateModel aSpringEndpointTemplateModel() throws IOException {
        return RestEndpointTemplateModel.builder()
                .basePackage(BASE_PACKAGE)
                .basePath("/petstore")
                .isWebMvc(true)
                .customProperties(endpointScopeCustomProperties())
                .resource(REST_RESOURCE)
                .route("/pet")
                .build();
    }

    Map<String, Object> projectScopeCustomProperties() throws IOException {
        return CustomPropertyAssembler.assembleCustomProperties(fakeArchetypeDescriptorFactory(), BASE_PACKAGE);
    }
    Map<String, Object> endpointScopeCustomProperties() throws IOException {
        return CustomPropertyAssembler.assembleCustomProperties(fakeArchetypeDescriptorFactory(), BASE_PACKAGE, REST_RESOURCE);
    }

    IArchetypeDescriptorFactory fakeArchetypeDescriptorFactory() throws IOException {
        PackageLayoutRuleSet plrs = packageLayoutRuleSet();
        ClassNameRuleSet cnrs = classNameRuleSet();
        return new ArchetypeDescriptorFactory(plrs, cnrs);
    }

    public static PackageLayoutRuleSet packageLayoutRuleSet() throws IOException {
        PackageLayoutReader reader = new PackageLayoutReader();
        PackageLayout layout = reader.read("classpath:/test-package-layout.json");
        Map<String, String> rules = PackageLayoutToHashMapMapper.convertToHashMap(layout);
        return new PackageLayoutRuleSet(rules);
    }

    public static ClassNameRuleSet classNameRuleSet() throws IOException {
        ClassNameRulesReader reader = new ClassNameRulesReader(
                new DefaultResourceLoader(),
                "classpath:/test-classname-rules.properties");
        Map<String, String> map = reader.read();
        return new ClassNameRuleSet(map);
    }

}
