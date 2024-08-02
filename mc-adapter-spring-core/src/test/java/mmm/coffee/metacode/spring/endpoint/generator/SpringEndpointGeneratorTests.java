/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.endpoint.generator;

import mmm.coffee.metacode.common.ExitCodes;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogEntryBuilder;
import mmm.coffee.metacode.common.catalog.TemplateFacetBuilder;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.exception.CreateEndpointUnsupportedException;
import mmm.coffee.metacode.common.io.MetaProperties;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.stereotype.Collector;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;
import mmm.coffee.metacode.spring.ClassNameRuleSetFixture;
import mmm.coffee.metacode.spring.MetaPropertiesHandlerFixture;
import mmm.coffee.metacode.spring.PackageLayoutRulesetFixture;
import mmm.coffee.metacode.spring.converter.NameConverter;
import mmm.coffee.metacode.spring.converter.RouteConstantsConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToTemplateModelConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.endpoint.mustache.MustacheEndpointDecoder;
import mmm.coffee.metacode.spring.FakeArchetypeDescriptorFactory;
import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * SpringEndpointGeneratorTests
 */
@SuppressWarnings("unchecked")
class SpringEndpointGeneratorTests {

    private static final String TEMPLATE_DIRECTORY = "/spring/templates/";
    final static String BASE_PATH = "/petstore";
    final static String BASE_PACKAGE = "org.acme.petstore";
    final static String WEBFLUX_FRAMEWORK = Framework.SPRING_WEBFLUX.frameworkName();
    final static String WEBMVC_FRAMEWORK = Framework.SPRING_WEBMVC.frameworkName();

    SpringEndpointGenerator generatorUnderTest;

    TemplateResolver<MetaTemplateModel> mockRenderer;

    MetaPropertiesHandler<RestEndpointDescriptor> mockMetaPropHandler;


    @Test
    void givenWebFluxFramework_shouldGenerateCode() throws IOException {
        generatorUnderTest = setUpGenerator(WEBFLUX_FRAMEWORK);
        var descriptor = RestEndpointDescriptor.builder().resource("Pet").route("/pet").build();

        int rc = generatorUnderTest.doPreprocessing(descriptor).generateCode(descriptor);
        assertThat(rc).isEqualTo(ExitCodes.OK);
    }

    @Test
    void givenWebMvcFramework_shouldGenerateCode() throws IOException {
        generatorUnderTest = setUpGenerator(WEBMVC_FRAMEWORK);
        var descriptor = RestEndpointDescriptor.builder().resource("Pet").route("/pet").build();

        int rc = generatorUnderTest.doPreprocessing(descriptor).generateCode(descriptor);
        assertThat(rc).isEqualTo(ExitCodes.OK);

    }

    @Test
    void whenFrameworkDoesNotSupportCreateEndpoint_expectCreateEndpointUnsupportedException() throws IOException {
        SpringEndpointGenerator springBootGenerator = setUpSpringBootGenerator();
        var descriptor = RestEndpointDescriptor.builder().resource("Pet").route("/pet").build();

        // We want the lambda to have only one method invoked that could possibly throw a runtime exception,
        // so part of the code happens outside the `assertThrows`.
        var generator = springBootGenerator.doPreprocessing(descriptor);
        assertThrows(CreateEndpointUnsupportedException.class, () -> {
            generator.generateCode(descriptor);
        });
    }

    // -------------------------------------------------------------------------------------
    //
    // Helper Methods
    //
    // -------------------------------------------------------------------------------------

    /**
     * Configures a SpringEndpointGenerator with suitably mocked components.
     * A code generator is, basically, a pipeline assembly consisting of
     * a handful of components that are assembled into the pipeline that
     * provides this flow: templates -> rendered content -> output files
     */
    @SuppressWarnings("java:S125") // false positive; there are no commented-out lines of code
    private SpringEndpointGenerator setUpGenerator(String frameworkToUse) throws IOException {
        // In the TemplateResolver, we just need the
        // {@code render} method to return a non-null String.
        // For these tests, we want to confirm the Generator's
        // 'pipeline' works.
        mockRenderer = Mockito.mock(TemplateResolver.class);
        when(mockRenderer.render(any(), any())).thenReturn("");

        // Mock a template resolver that returns an empty string as the rendered content
        var mockTemplateResolver = Mockito.mock(TemplateResolver.class);
        when(mockTemplateResolver.render(any(), any())).thenReturn("");

        // Mock a Configuration that will return values that are
        // normally acquired by reading the metacode.properties file.
        Configuration mockConfig = Mockito.mock(Configuration.class);
        when(mockConfig.getString(MetaProperties.BASE_PATH)).thenReturn(BASE_PATH);
        when(mockConfig.getString(MetaProperties.BASE_PACKAGE)).thenReturn(BASE_PACKAGE);
        when(mockConfig.getString(MetaProperties.FRAMEWORK)).thenReturn(frameworkToUse);

        // Set up the MetaPropertiesHandler. We only have to mock reading;
        // endpoint code generation never writes to the metacode.properties file.
        mockMetaPropHandler = Mockito.mock(MetaPropertiesHandler.class);
        when(mockMetaPropHandler.readMetaProperties()).thenReturn(mockConfig);

        // Set up a MustacheEndpointConverter
        var converter = new RestEndpointTemplateModelToMapConverter();
        var mustacheEndpointDecoder = MustacheEndpointDecoder.builder()
                .converter(converter).build();

        var mockOutputHandler = Mockito.mock(WriteOutputTrait.class);
        doNothing().when(mockOutputHandler).writeOutput(anyString(), anyString());

        // Finally, assemble the above components into a code generator
        return SpringEndpointGenerator.builder()
                .collector(new FakeCollector())
                .descriptor2predicate(new RestEndpointDescriptorToPredicateConverter())
                .descriptor2templateModel(new RestEndpointDescriptorToTemplateModelConverter(new NameConverter(), new RouteConstantsConverter()))
                .metaPropertiesHandler(mockMetaPropHandler)
                .mustacheDecoder(mustacheEndpointDecoder)
                .templateRenderer(mockTemplateResolver)
                .outputHandler(mockOutputHandler)
                .archetypeDescriptorFactory(new FakeArchetypeDescriptorFactory())
                .build();
    }

    private SpringEndpointGenerator setUpSpringBootGenerator() throws IOException {
        return setUpGenerator(Framework.SPRING_BOOT.name());
    }

    /**
     * A fake Collector suitable for unit test usage
     */
    public static class FakeCollector implements Collector {

        /**
         * Builds a data set of CatalogEntry's
         */
        private static List<CatalogEntry> buildSampleSet() {
            CatalogEntry e1 = buildEntry("Service.ftl", "Service.java");
            CatalogEntry e2 = buildEntry("Controller.ftl", "Controller.java");
            CatalogEntry e3 = buildEntry("Repository.ftl", "Repository.java");
            CatalogEntry e4 = buildEntry("ServiceTest.ftl", "ServiceTest.java");
            CatalogEntry e5 = buildEntry("ControllerIT.ftl", "ControllerTest.java");

            return List.of(e1, e2, e3, e4, e5);
        }

        /**
         * Builds a single CatalogEntry
         */
        private static CatalogEntry buildEntry(String source, String destination) {
            return CatalogEntryBuilder.builder()
                    .addFacet(TemplateFacetBuilder.builder()
                            .source(source)
                            .destination(destination)
                            .build())
                    .scope(MetaTemplateModel.Key.ENDPOINT.value())
                    .tags(null)
                    .build();
        }

        /**
         * Collects items, honoring the conditions set with {@code setConditions}
         *
         * @return the items meeting the conditions
         */
        @Override
        public List<CatalogEntry> collect() {
            return buildSampleSet();
        }
    }


    ClassNameRuleSet buildClassNameRuleSet() throws IOException {
        return ClassNameRuleSetFixture.buildClassNameRuleSet();
    }

    PackageLayoutRuleSet buildPackageLayoutRuleSet() throws IOException {
        return PackageLayoutRulesetFixture.buildPackageLayoutRuleSet();
    }

    MetaPropertiesHandler<RestEndpointDescriptor> buildMockMetaPropertiesHandler(String frameworkToUse) {
        return MetaPropertiesHandlerFixture.buildMockMetaPropertiesHandler(frameworkToUse);
    }

}
