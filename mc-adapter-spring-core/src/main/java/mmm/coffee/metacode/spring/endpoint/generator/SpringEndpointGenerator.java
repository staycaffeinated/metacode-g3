/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.endpoint.generator;

import com.google.common.base.Predicate;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.ExitCodes;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.exception.CreateEndpointUnsupportedException;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import mmm.coffee.metacode.common.io.MetaProperties;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.stereotype.Collector;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;
import mmm.coffee.metacode.spring.endpoint.model.RestEndpointTemplateModel;
import mmm.coffee.metacode.spring.endpoint.mustache.MustacheEndpointDecoder;
import mmm.coffee.metacode.spring.project.generator.CustomPropertyAssembler;
import mmm.coffee.metacode.spring.project.generator.OutputFileDestinationResolver;
import org.apache.commons.configuration2.Configuration;

/**
 * SpringEndpointGenerator
 */
@SuperBuilder
@Slf4j
@SuppressWarnings({"java:S4738", "java:S1602", "java:S125"})
// S4738: accepting Google Predicate class for now
// S1602: false positive: comment around line 82 happens to look like a lambda function
// S125: we're OK with comments that look like code
public class SpringEndpointGenerator implements ICodeGenerator<RestEndpointDescriptor> {

    final MetaPropertiesHandler<RestEndpointDescriptor> metaPropertiesHandler;
    private final ConvertTrait<RestEndpointDescriptor, RestEndpointTemplateModel> descriptor2templateModel;
    private final ConvertTrait<RestEndpointDescriptor, Predicate<CatalogEntry>> descriptor2predicate;
    private final TemplateResolver<MetaTemplateModel> templateRenderer;
    private final WriteOutputTrait outputHandler;
    private final MustacheEndpointDecoder mustacheDecoder;
    // At this time, we don't know which Collector to use until the doPreprocessing
    // method is called, because we have one collector for WebMvc templates, and a
    // different collector for WebFlux. This choice is worth revisiting.
    // NB: Perhaps use a Strategy object to handle collection based on style, webmvc vs flux
    private Collector collector;

    private final IArchetypeDescriptorFactory archetypeDescriptorFactory;


    /**
     * Fills in data missing from the {@code spec} using the {@code metacode.properties}
     * as the source of data for those missing pieces.
     * <p>
     * For example, when creating an endpoint, the end-user does not type in the
     * base package since we can acquire the base package from the {@code metacode.properties}
     * file. The general motivation is to keep the CLI simple,
     *
     * @param spec contains the values entered on the command line -- the resourceName
     *             and resourcePath
     * @return an updated {@code spec} with the basePackage, basePath, and framework.
     */
    @Override
    public ICodeGenerator<RestEndpointDescriptor> doPreprocessing(RestEndpointDescriptor spec) {
        log.info("[doPreprocessing] on entry, spec.tableName ={}", spec.getTableName());
        Configuration config = metaPropertiesHandler.readMetaProperties();
        spec.setBasePackage(config.getString(MetaProperties.BASE_PACKAGE));
        spec.setBasePath(config.getString(MetaProperties.BASE_PATH));
        spec.setFramework(config.getString(MetaProperties.FRAMEWORK));
        spec.setSchema(config.getString(MetaProperties.SCHEMA));
        spec.setWithLiquibase(config.getBoolean(MetaProperties.ADD_LIQUIBASE, false));
        spec.setWithPostgres(config.getBoolean(MetaProperties.ADD_POSTGRESQL, false));
        spec.setWithTestContainers(config.getBoolean(MetaProperties.ADD_TESTCONTAINERS, false));
        spec.setWithMongoDb(config.getBoolean(MetaProperties.ADD_MONGODB, false));
        spec.setWithOpenApi(config.getBoolean(MetaProperties.ADD_OPENAPI, false));
        spec.setWithKafka(config.getBoolean(MetaProperties.ADD_KAFKA, false));

        log.info("[doPreprocessing] on exit, spec.tableName ={}", spec.getTableName());


        return this;
    }

    /**
     * Performs the code generation. Returns:
     * 0 = success
     * 1 = general error
     *
     * @param descriptor the endpoint metadata that tells us the resource and route to create
     * @return the exit code, with zero indicating success.
     */
    @SuppressWarnings("java:S1135") // ignore todo stuff for now
    @Override
    public int generateCode(RestEndpointDescriptor descriptor) {
        if (!frameworkIsSupported(descriptor.getFramework())) {
            throw new CreateEndpointUnsupportedException(String.format("The `create endpoint` command not supported by the %s project template", descriptor.getFramework()));
        }

        // Build the TemplateModel consumed by Freemarker to resolve template variables
        var templateModel = descriptor2templateModel.convert(descriptor);

        log.info("[generateCode] tableName at entry is {}", templateModel.getTableName());

        templateModel.setCustomProperties(CustomPropertyAssembler.assembleCustomProperties(
                archetypeDescriptorFactory,
                descriptor.getBasePackage(),
                templateModel.getResource()));

        // Create a predicate to determine which template's to render
        Predicate<CatalogEntry> keepThese = descriptor2predicate.convert(descriptor);

        // Provide state to the MustacheDecoder so that mustache variables can be resolved.
        mustacheDecoder.configure(templateModel);

        log.info("[generateCode] collector is-a {}", collector.getClass().getName());
        log.info("[generateCode] outputHandler is-a {}", outputHandler.getClass().getName());
        log.info("[generateCode] tableName before rendering is {}", templateModel.getTableName());

        // Render the templates
        collector.prepare(descriptor).collect().stream().parallel().filter(keepThese).forEach(catalogEntry -> {
            catalogEntry.getFacets().forEach(facet -> {
                String renderedContent = templateRenderer.render(facet.getSourceTemplate(), templateModel);
                if (catalogEntry.getArchetype() != null) {
                    String outputFileName = OutputFileDestinationResolver.resolveDestination(
                            facet,
                            catalogEntry.getArchetype(),
                            templateModel,
                            mustacheDecoder);
                    log.info("Resolved destination: archetype: {}, destination: {}", catalogEntry.getArchetype(), outputFileName);
                    outputHandler.writeOutput(outputFileName, renderedContent);
                }
            });
        });

        return ExitCodes.OK;
    }

    /**
     * Verify the endpoint is being created within a project template that supports endpoints.
     */
    boolean frameworkIsSupported(final String framework) {
        // It only makes sense to generate endpoints for WebMvc and WebFlux projects.
        // For the SpringBoot template, too many expected classes would be missing for this to work.
        return (framework.equalsIgnoreCase(Framework.SPRING_WEBMVC.frameworkName()) ||
                framework.equalsIgnoreCase(Framework.SPRING_WEBFLUX.frameworkName()));
    }
}
