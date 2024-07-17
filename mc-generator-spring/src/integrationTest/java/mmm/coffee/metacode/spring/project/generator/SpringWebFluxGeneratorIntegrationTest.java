/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.ExitCodes;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.freemarker.ConfigurationFactory;
import mmm.coffee.metacode.common.freemarker.FreemarkerTemplateResolver;
import mmm.coffee.metacode.common.writer.ContentToNullWriter;
import mmm.coffee.metacode.spring.catalog.SpringWebFluxTemplateCatalog;
import mmm.coffee.metacode.spring.constant.SpringIntegrations;
import mmm.coffee.metacode.spring.project.converter.DescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.project.converter.DescriptorToTemplateModelConverter;
import mmm.coffee.metacode.spring.project.converter.RestTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;


/**
 * SpringGeneratorIntegrationTest
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
class SpringWebFluxGeneratorIntegrationTest {

    private static final String DEPENDENCY_FILE = "/spring/dependencies/dependencies.yml";
    private static final String TEMPLATE_DIRECTORY = "/spring/templates/";

    private static final String APP_NAME = "petstore";      // thought: change to 'artifactName'?
    private static final String BASE_PATH = "/petstore";
    private static final String BASE_PKG = "org.acme.petstore";

    SpringProjectCodeGenerator generatorUnderTest;

    /**
     * Configure the code generator under test
     */
    @BeforeEach
    public void setUp() throws IOException {
        generatorUnderTest = SpringProjectCodeGenerator.builder()
                .collector(new SpringWebFluxTemplateCatalog(new CatalogFileReader()))
                .descriptor2templateModel(new DescriptorToTemplateModelConverter())
                .descriptor2predicate(new DescriptorToPredicateConverter())
                .templateRenderer(new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY)))
                .outputHandler(new ContentToNullWriter())
                .dependencyCatalog(new DependencyCatalog(DEPENDENCY_FILE))
                .archetypeDescriptorFactory(new FakeArchetypeDescriptorFactory())
                .mustacheDecoder(
                        MustacheDecoder.builder()
                                .converter(new RestTemplateModelToMapConverter()).build())
                .build();
    }

    /**
     * Verify a project with the minimum required properties
     * can be generated.
     */
    @ParameterizedTest
    @MethodSource("frameworks")
    void whenSimpleProject_shouldRenderTemplate(Framework framework) {
        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePackage(BASE_PATH)
                .framework(framework)
                .build();

        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    /**
     * Verify a project that includes Postgres integration can be generated.
     */
    @ParameterizedTest
    @MethodSource("frameworks")
    void whenPostgresIntegration_shouldRenderTemplate(Framework framework) {
        // given: postgres integration is enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.POSTGRES.name());
        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .framework(framework)
                .integrations(integrations)
                .build();

        // when: generating code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }


    @Test
    void whenMongoDbIntegration_shouldRenderTemplates() {
        // given: MongoDB and TestContainer integration is enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.MONGODB.name(),
                SpringIntegrations.TESTCONTAINERS.name());
        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .framework(Framework.SPRING_WEBMVC)
                .integrations(integrations)
                .build();

        // when: generating code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    /**
     * Verify a project that includes TestContainers integration can be generated.
     */
    @Test
    void whenTestContainersIntegration_shouldRenderTemplate() {
        // given: testcontainers integration is enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.TESTCONTAINERS.name());
        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .framework(Framework.SPRING_WEBMVC)
                .integrations(integrations)
                .build();

        // when: generating the code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    /**
     * Verify a project that includes Postgres + TestContainers integrations can be generated.
     */
    @Test
    void whenTestContainersAndPostgresIntegration_shouldRenderTemplate() {
        // given: testcontainers and postgres integration is enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.TESTCONTAINERS.name(),
                SpringIntegrations.POSTGRES.name());

        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .framework(Framework.SPRING_WEBMVC)
                .integrations(integrations)
                .build();

        // when: generating code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    /**
     * Verify a project that includes Liquibase integration can be generated.
     */
    @Test
    void whenLiquibaseIntegration_shouldRenderTemplate() {
        // given: liquibase integration is enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.LIQUIBASE.name());
        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .integrations(integrations)
                .framework(Framework.SPRING_WEBMVC)
                .build();

        // when: generating code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    /**
     * Verify a project that includes Liquibase integration can be generated.
     */
    @Test
    void whenPostgresAndTestContainersAndLiquibaseIntegration_shouldRenderTemplate() {
        // given: postgres, testcontainers, and liquibase integrations are enabled
        Set<String> integrations = buildIntegrations(SpringIntegrations.POSTGRES.name(),
                SpringIntegrations.TESTCONTAINERS.name(),
                SpringIntegrations.LIQUIBASE.name());

        var spec = RestProjectDescriptor.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .integrations(integrations)
                .framework(Framework.SPRING_WEBMVC)
                .build();

        // when: generating code, expect success
        assertThat(generatorUnderTest.generateCode(spec)).isEqualTo(ExitCodes.OK);
    }

    // ------------------------------------------------------------------------------------------
    //
    // Helper methods
    //
    // ------------------------------------------------------------------------------------------

    /**
     * Converts the given var-args of integration options into a set of String
     *
     * @param args the integration options
     * @return the equivalent Set
     */
    private Set<String> buildIntegrations(String... args) {
        return new HashSet<>(Arrays.stream(args).toList());
    }

    /**
     * Returns the frameworks to test here
     */
    private Stream<Arguments> frameworks() {
        return Stream.of(
                Arguments.arguments(Framework.SPRING_WEBFLUX)
        );
    }

}
