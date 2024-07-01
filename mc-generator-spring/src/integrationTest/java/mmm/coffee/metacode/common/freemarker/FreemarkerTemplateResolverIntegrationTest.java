/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.common.freemarker;

import com.google.common.base.Predicate;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.dictionary.ProjectArchetypeToMap;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.stereotype.Collector;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import mmm.coffee.metacode.spring.catalog.SpringWebMvcTemplateCatalog;
import mmm.coffee.metacode.spring.project.converter.DescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.project.generator.CustomPropertyAssembler;
import mmm.coffee.metacode.spring.project.generator.FakeArchetypeDescriptorFactory;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * FreemarkerTemplateResolverIT
 */
@SuppressWarnings("DuplicatCode")
class FreemarkerTemplateResolverIntegrationTest {

    private static final String TEMPLATE_FOLDER = "/spring/templates";
    private static final String APP_NAME = "petstore";
    private static final String BASE_PATH = "/petstore";
    private static final String BASE_PKG = "acme.petstore";

    private static final String DEPENDENCY_FILE = "/spring/dependencies/dependencies.yml";

    final FreemarkerTemplateResolver resolverUnderTest = new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(TEMPLATE_FOLDER));
    final DependencyCatalog dependencyCatalog = new DependencyCatalog(DEPENDENCY_FILE);
    final ConvertTrait<RestProjectDescriptor, Predicate<CatalogEntry>> converter = new DescriptorToPredicateConverter();
    /*
     * Use the Spring templates for test coverage
     */
    final Collector templateCatalog = new SpringWebMvcTemplateCatalog(new CatalogFileReader());
    RestProjectTemplateModel webMvcProject;
    RestProjectTemplateModel webFluxProject;
    RestProjectDescriptor projectDescriptor;

    /***
     * Wraps a Google Predicate in a java.util.function.Predicate
     */
    public static <T> java.util.function.Predicate<T> toJava8(com.google.common.base.Predicate<T> guavaPredicate) {
        return (guavaPredicate::apply);
    }

    @BeforeEach
    public void setUpTemplateModel() throws Exception {
        webMvcProject = RestProjectTemplateModel.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .isWebMvc(true)
                .build();
        
        Map<String,Object> customProps = CustomPropertyAssembler.assembleCustomProperties(new FakeArchetypeDescriptorFactory(), BASE_PKG);
        webMvcProject.setCustomProperties(customProps);

        webFluxProject = RestProjectTemplateModel.builder()
                .applicationName(APP_NAME)
                .basePackage(BASE_PKG)
                .basePath(BASE_PATH)
                .isWebFlux(true)
                .build();
        webFluxProject.setCustomProperties(customProps);

        // Set up our base project model
        projectDescriptor = RestProjectDescriptor.builder()
                .applicationName("petstore")
                .basePackage("acme.petstore")
                .basePath("/petstore")
                .build();
    }

    /**
     * This test takes a RestTemplateModel and uses it to render every WebMvc-based template.
     * This test will tell us if a template has an undefined variable, which needs
     * to be added to the template model. Freemarker throws an exception whenever
     * an undefined variable is encountered in a template. To ensure our template model
     * defines every variable that might be consumed by a template, we have this
     * test case try every template.
     */
    @Test
    void shouldRenderWebMvcProjectTemplates() {
        // prelim: create a Predicate to include project templates
        // and exclude endpoint templates.
        Predicate<CatalogEntry> keepThese = converter.convert(projectDescriptor);
        webMvcProject.configureLibraryVersions(dependencyCatalog);

        templateCatalog.collect().stream().filter(toJava8(keepThese)).forEach(template -> {
            for (TemplateFacet facet : template.getFacets()) {
                var content = resolverUnderTest.render(facet.getSourceTemplate(), webMvcProject);
                // The rendered `schema.sql` file _can_ be blank, so make an exception for that.
                // Otherwise, ensure something was rendered
                if (!facet.getSourceTemplate().endsWith("SchemaDotSql.ftl")) {
                    assertThat(content).isNotEmpty();
                }
            }
        });
    }

    
    /**
     * This test takes a RestTemplateModel and uses it to render every WebMvc-based template.
     */
    @Test
    void shouldRenderWebFluxProjectTemplates() {
        // prelim: create a Predicate to include project templates
        // and exclude endpoint templates.
        Predicate<CatalogEntry> keepThese = converter.convert(projectDescriptor);
        webFluxProject.configureLibraryVersions(dependencyCatalog);

        templateCatalog.collect().stream().filter(toJava8(keepThese)).forEach(template -> {
            for (TemplateFacet facet : template.getFacets()) {
                var content = resolverUnderTest.render(facet.getSourceTemplate(), webFluxProject);
                // The rendered `schema.sql` file _can_ be blank, so make an exception for that.
                // Otherwise, ensure something was rendered
                if (!facet.getSourceTemplate().endsWith("SchemaDotSql.ftl")) {
                    assertThat(content).isNotEmpty();
                }
            }
        });
    }
}
