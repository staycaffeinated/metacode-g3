/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.project.generator;

import freemarker.template.Configuration;
import mmm.coffee.metacode.common.freemarker.ConfigurationFactory;
import mmm.coffee.metacode.common.freemarker.FreemarkerTemplateResolver;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * This collection of tests help us verify the content
 * rendered by the TemplateResolver contains expected content.
 * <p>
 * These tests are helpful for verifying the expression syntax in
 * the templates, especially relative to the template model.
 * For example, if the templateModel has the field 'testContainer'
 * but the template is expecting 'templatecontainer', this group of
 * tests can catch those errors.
 */
class SpringWebMvcTemplatesRenderingTests {

    private static final String TEMPLATE_DIRECTORY = "/spring/templates/";

    private final Configuration configuration = ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY);

    private final TemplateResolver<MetaTemplateModel> templateResolver = new FreemarkerTemplateResolver(configuration);

    /**
     * Builds a RestProjectTemplateModel populated with properties
     * likely to be needed across several tests
     */
    private RestProjectTemplateModel buildBasicModel() {
        // the version numbers here are hypothetical
        return RestProjectTemplateModel.builder()
                .applicationName("bookstore")
                .basePackage("acme.books")
                .basePath("/store")
                .build();


    }

    @Nested
    class BuildDotGradleTests {

        // This path is relative to TEMPLATE_DIRECTORY
        final String template = "/gradle/BuildDotGradle.ftl";

        //
        // This series confirms behavior when flags _are_ enabled
        //
        @Test
        void whenPostgresFlagIsEnabled_expectUsesPostgresLibrary() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("libs.postgresql");
            // make sure startWeb and not starterWebFlux is used
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenTestContainerFlagIsEnabled_expectUsesTestContainerLibrary() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithTestContainers(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("libs.spring.boot.testcontainers");
            assertThat(content).contains("libs.testcontainers.jupiter");
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenLiquibaseFlagIsEnabled_expectUsesLiquibaseLibrary() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithLiquibase(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("libs.liquibase.core");
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenLiquibaseAndPostgresAreEnabled_expectUsesPostgresTestContainerLibrary() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithTestContainers(true);
            templateModel.setWithPostgres(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("libs.testcontainers.postgres");
            // also verify webmvc libs are used, not webflux libs
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        //
        // This series confirms behavior when flags _are not_ set. For example, when an integration
        // is not requested, the integration library should not be present.
        //
        @Test
        void whenPostgresFlagIsNotEnabled_expectPostgresLibraryIsAbsent() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).doesNotContain("libs.postgresql");
            // check for presence of webmvc libs
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenLiquibaseFlagIsNotEnabled_expectLiquibaseLibraryIsAbsent() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).doesNotContain("libs.liquibase.core");
            // check for presence of webmvc libs
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenTestContainersFlagIsNotEnabled_expectTestContainersLibraryIsAbsent() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).doesNotContain("libs.spring.boot.testcontainers");
            assertThat(content).doesNotContain("libs.testcontainers.jupiter");
            // check for presence of webmvc libs
            assertThat(content).contains("libs.spring.boot.starter.web");
            assertThat(content).contains("libs.spring.boot.starter.data.jpa");
        }

        @Test
        void whenWebFlux_expectSpringBootStarterWebFluxIsDependency() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebFlux(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("libs.spring.boot.starter.data.r2dbc");
            assertThat(content).contains("libs.spring.boot.starter.webflux");
        }

    }

    @Nested
    class ApplicationDotPropertiesTests {
        // This path is relative to TEMPLATE_DIRECTORY
        final String template = "/spring/webmvc/main/resources/ApplicationDotProperties.ftl";

        @Test
        void whenPostgresTrue_expectPostgresJdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            assertThat(content).isNotNull();
            assertThat(content).contains("url: jdbc:postgresql:");
            assertThat(content).contains("driver-class-name: org.postgresql.Driver");
        }

        @Test
        void whenPostgresFlagIsNotEnabled_expectH2JdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            // When no database is specified, the templates default to the H2 in-memory database
            assertThat(content).isNotNull();
            assertThat(content).contains("url: jdbc:h2:mem");
            assertThat(content).contains("driver-class-name: org.h2.Driver");
        }
    }

    @Nested
    class ApplicationTestDotYamlTests {
        // This path is relative to TEMPLATE_DIRECTORY
        final String template = "/spring/webmvc/test/resources/ApplicationTestDotYaml.ftl";

        @Disabled("Move to SpringWebMvc")
        void whenPostgresFlagEnabled_expectPostgresJdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            // these are some strings expected to be found.
            assertThat(content).isNotNull();
            assertThat(content).contains("driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver");
            assertThat(content).contains("url: jdbc:tc:postgresql:9.6.8:///testdb?currentSchema=public");
            assertThat(content).contains("dialect: org.hibernate.dialect.PostgreSQLDialect");
            assertThat(content).contains("database: POSTGRESQL");
        }

        @Test
        void whenPostgresFlagIsNotEnabled_expectH2JdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            // When no database is specified, the templates default to the H2 in-memory database
            assertThat(content).isNotNull();
            assertThat(content).contains("driver-class-name: \"org.h2.Driver\"");
            assertThat(content).contains("url: \"jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1\"");
            assertThat(content).contains("database-platform: \"org.hibernate.dialect.H2Dialect\"");
        }
    }

    // ------------------------------------------------------------------------------------------------
    //
    // Helper Methods
    //
    // ------------------------------------------------------------------------------------------------

    @Nested
    class DockerComposeTests {
        // This path is relative to TEMPLATE_DIRECTORY
        final String template = "/docker/DockerCompose.ftl";

        @Test
        void whenPostgresFlagEnabled_expectPostgresJdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(true);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            // these are some strings expected to be found.
            assertThat(content).isNotNull();
            assertThat(content).contains("SPRING_DATASOURCE_URL=jdbc:postgresql");
            assertThat(content).contains("SPRING_DATASOURCE_USERNAME=postgres");
        }

        @Test
        void whenPostgresFlagIsNotEnabled_expectH2JdbcDriver() {
            RestProjectTemplateModel templateModel = buildBasicModel();
            templateModel.setWithPostgres(false);
            templateModel.setWebMvc(true);

            String content = templateResolver.render(template, templateModel);

            // When Postgres is not specified, the Postgres stanza should not be found.
            // Spot check for various Postgres properties to ensure they are not present.
            assertThat(content).isNotNull();
            assertThat(content).doesNotContain("SPRING_DATASOURCE_URL=jdbc:postgresql");
            assertThat(content).doesNotContain("SPRING_DATASOURCE_USERNAME=postgres");
        }
    }


}
