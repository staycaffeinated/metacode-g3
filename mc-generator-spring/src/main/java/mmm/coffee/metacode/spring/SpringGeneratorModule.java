/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.spring;

import com.google.inject.AbstractModule;
import freemarker.template.Configuration;
import mmm.coffee.metacode.annotations.guice.*;
import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.freemarker.ConfigurationFactory;
import mmm.coffee.metacode.common.freemarker.FreemarkerTemplateResolver;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import mmm.coffee.metacode.common.io.MetaProperties;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.io.MetaPropertiesReader;
import mmm.coffee.metacode.common.io.MetaPropertiesWriter;
import mmm.coffee.metacode.common.stereotype.DependencyCollector;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;
import mmm.coffee.metacode.common.writer.ContentToFileWriter;
import mmm.coffee.metacode.spring.catalog.SpringEndpointCatalog;
import mmm.coffee.metacode.spring.catalog.SpringWebFluxTemplateCatalog;
import mmm.coffee.metacode.spring.catalog.SpringWebMvcTemplateCatalog;
import mmm.coffee.metacode.spring.converter.NameConverter;
import mmm.coffee.metacode.spring.converter.RouteConstantsConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToTemplateModelConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.endpoint.generator.SpringEndpointGenerator;
import mmm.coffee.metacode.spring.endpoint.io.SpringEndpointMetaPropertiesHandler;
import mmm.coffee.metacode.spring.endpoint.mustache.MustacheEndpointDecoder;
import mmm.coffee.metacode.spring.project.converter.DescriptorToMetaProperties;
import mmm.coffee.metacode.spring.project.converter.DescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.project.converter.DescriptorToTemplateModelConverter;
import mmm.coffee.metacode.spring.project.converter.RestTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.project.generator.SpringProjectCodeGenerator;
import mmm.coffee.metacode.spring.project.io.SpringMetaPropertiesHandler;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.springframework.context.annotation.Bean;

/**
 * Module for the Spring Project generator
 * <p>
 * IMPORTANT:
 * If a new `provides` method is added here, make sure a matching method
 * is added to the SpringTestModule class. Otherwise, you will encounter
 * runtime errors during testing, like 'picocli.CommandLine$InitializationException: Could not inject spec'.
 * The SpringTestModule is used by the integration tests to fake some of the dependencies
 * For example, to prevent the tests from writing files to the file system, a NullWriter is wired
 * into the SpringTestModule, while a real Writer is wired into this class, the SpringGeneratorModule.
 */
@SuppressWarnings("java:S1452") // S1452: allow generic wildcards for the moment
@ExcludeFromJacocoGeneratedReport // Ignore code coverage for this class
@org.springframework.context.annotation.Configuration
public class SpringGeneratorModule extends AbstractModule {

    private static final String DEPENDENCY_FILE = "/spring/dependencies/dependencies.yml";
    private static final String TEMPLATE_DIRECTORY = "/spring/templates/";

    /**
     * SpringWebMvc generator
     */
    @Bean("springWebmvcGenerator")
    @SpringWebMvc
    ICodeGenerator<RestProjectDescriptor> providesSpringWebMvcGenerator(PackageLayoutRuleSet packageLayoutRuleSet, ClassNameRuleSet classNameRuleSet) {
        return SpringProjectCodeGenerator.builder()
                .collector(new SpringWebMvcTemplateCatalog(new CatalogFileReader()))
                .descriptor2templateModel(new DescriptorToTemplateModelConverter())
                .descriptor2predicate(new DescriptorToPredicateConverter())
                .templateRenderer(new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY)))
                .outputHandler(new ContentToFileWriter())
                .dependencyCatalog(new DependencyCatalog(DEPENDENCY_FILE))
                .mustacheDecoder(
                        MustacheDecoder.builder()
                                .converter(new RestTemplateModelToMapConverter()).build())
                .metaPropertiesHandler(providesMetaPropertiesHandler())
                .archetypeDescriptorFactory(new ArchetypeDescriptorFactory(packageLayoutRuleSet, classNameRuleSet))
                .build();
    }

    @Bean("springWebFluxGenerator")
    @SpringWebFlux
    ICodeGenerator<RestProjectDescriptor> providesSpringWebFluxGenerator() {
        return SpringProjectCodeGenerator.builder()
                .collector(new SpringWebFluxTemplateCatalog(new CatalogFileReader()))
                .descriptor2templateModel(new DescriptorToTemplateModelConverter())
                .descriptor2predicate(new DescriptorToPredicateConverter())
                .templateRenderer(new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY)))
                .outputHandler(new ContentToFileWriter())
                .dependencyCatalog(new DependencyCatalog(DEPENDENCY_FILE))
                .mustacheDecoder(
                        MustacheDecoder.builder()
                                .converter(new RestTemplateModelToMapConverter()).build())
                .metaPropertiesHandler(providesMetaPropertiesHandler())
                .build();
    }

    /**
     * FreeMarkerConfiguration
     */
    @Bean
    @FreemarkerConfigurationProvider
    Configuration providesFreemarkerConfiguration() {
        // The resource path to the Freemarker templates will be different for each
        // code generator that's plugged in. For instance, Micronaut templates will
        // be in a different folder than the Spring templates.  When the Configuration
        // object is created, the base folder used by the Configuration instance varies.
        return ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY);
    }

    /**
     * The code generator needs a class that will handle writing content to a file.
     * Specifically, once a template is parsed and rendered as a String, that String
     * gets written to a file (such as a .java source file). The WriteOutputProvider
     * provides the class that handles that.
     * <p>
     * The WriteOutputProvider is made "pluggable" so that, during testing, a NullWriter
     * can be used to exercise the code w/o producing files that need to be cleaned up
     * after the tests finish.
     */
    @Bean
    @WriteOutputProvider
    WriteOutputTrait providesWriteOutput() {
        return new ContentToFileWriter();
    }

    @Bean
    @DependencyCatalogProvider
    DependencyCollector providesDependencyCatalog() {
        return new DependencyCatalog(DEPENDENCY_FILE);
    }

    @Bean
    @MetaPropertiesHandlerProvider
    public MetaPropertiesHandler<RestProjectDescriptor> providesMetaPropertiesHandler() {
        return SpringMetaPropertiesHandler.builder()
                .converter(new DescriptorToMetaProperties())
                .reader(MetaPropertiesReader.builder()
                        .propertyFileName(MetaProperties.DEFAULT_FILENAME)
                        .configurations(new Configurations())
                        .build())
                .writer(MetaPropertiesWriter.builder()
                        .destinationFile(MetaProperties.DEFAULT_FILENAME)
                        .configuration(new PropertiesConfiguration())
                        .build())
                .build();
    }

    @Bean("restEndpointGenerator")
    @RestEndpointGeneratorProvider
    ICodeGenerator<RestEndpointDescriptor> providesRestEndpointGenerator(PackageLayoutRuleSet packageLayoutRuleSet, ClassNameRuleSet classNameRuleSet) {
        var converterO1 = new RestEndpointTemplateModelToMapConverter();

        return SpringEndpointGenerator.builder()
                .collector(new SpringEndpointCatalog(new CatalogFileReader()))
                .descriptor2predicate(new RestEndpointDescriptorToPredicateConverter())
                .descriptor2templateModel(new RestEndpointDescriptorToTemplateModelConverter(new NameConverter(), new RouteConstantsConverter()))
                .metaPropertiesHandler(providesEndpointMetaPropertiesHandler())
                .mustacheDecoder(MustacheEndpointDecoder.builder()
                        .converter(converterO1)
                        .build())
                .outputHandler(providesWriteOutput())
                .templateRenderer(new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(TEMPLATE_DIRECTORY)))
                .archetypeDescriptorFactory(new ArchetypeDescriptorFactory(packageLayoutRuleSet, classNameRuleSet))
                .build();
    }

    @Bean
    @EndpointMetaPropertiesHandlerProvider
    MetaPropertiesHandler<RestEndpointDescriptor> providesEndpointMetaPropertiesHandler() {
        return SpringEndpointMetaPropertiesHandler.builder()
                .reader(MetaPropertiesReader.builder()
                        .propertyFileName(MetaProperties.DEFAULT_FILENAME)
                        .configurations(new Configurations())
                        .build())
                .build();
    }
}
