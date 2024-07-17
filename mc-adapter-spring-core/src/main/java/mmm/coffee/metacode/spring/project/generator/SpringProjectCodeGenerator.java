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
package mmm.coffee.metacode.spring.project.generator;

import com.google.common.base.Predicate;
import com.samskivert.mustache.MustacheException;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.ExitCodes;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.ProjectArchetypeToMap;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;
import mmm.coffee.metacode.common.rule.PackageNameConversion;
import mmm.coffee.metacode.common.stereotype.Collector;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModelFactory;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Code generator for SpringWebMvc project
 */
@Slf4j
@SuperBuilder
@SuppressWarnings({
        "java:S1068",   // this is a work-in-progress, so unused stuff is OK
        "java:S1602",   // false positive; curly braces in a comment do not mean it's a commented-out lambda function
        "java:S125",    // we're OK with comments that happen to look like code
        "java:S4738"    // migrating to java.util.function.Predicate is on the roadmap
})
public class SpringProjectCodeGenerator implements ICodeGenerator<RestProjectDescriptor> {

    private final Collector collector;
    private final ConvertTrait<RestProjectDescriptor, RestProjectTemplateModel> descriptor2templateModel;
    private final ConvertTrait<RestProjectDescriptor, Predicate<CatalogEntry>> descriptor2predicate;
    private final TemplateResolver<MetaTemplateModel> templateRenderer;
    private final WriteOutputTrait outputHandler;
    private final DependencyCatalog dependencyCatalog;
    private final MustacheDecoder mustacheDecoder;
    private final MetaPropertiesHandler<RestProjectDescriptor> metaPropertiesHandler;
    private final IArchetypeDescriptorFactory archetypeDescriptorFactory;

    /*
     * An instance of a RestProjectDescriptor is almost never available
     * when this object's builder methods are called.
     */
    private RestProjectDescriptor descriptor;

    /**
     * Writes the metacode.properties file
     *
     * @param descriptor provides the values to be saved to metacode.properties
     * @return {@code this} object
     */
    public SpringProjectCodeGenerator doPreprocessing(RestProjectDescriptor descriptor) {
        metaPropertiesHandler.writeMetaProperties(descriptor);
        return this;
    }

    /**
     * Returns the exit code from the generator.
     * 0 = success
     * 1 = general error
     *
     * @return the exit code, with zero indicating success.
     * <p>
     * Hypothetically, can we do something like:
     * <code>
     * Function<ProjectRestDescriptor,TemplateModel> mapToTemplateModel => (x) -> map(x)
     * Function<TemplateModel,DependencyCatalog,TemplateModel> mapToTemplateModel => (x,y) -> map(x,y)
     * Function<ProjectRestDescriptor,Predicate> mapToPredicate => (x) -> map(x)
     * <p>
     * var templateModel = TemplateModelBuilder.builder()
     * .usingProjectDescriptor(restProjectDescriptor)
     * .usingLibraryVersions(dependencyCatalog)
     * .usingPackageSchema(packageSchema)
     * .build();
     * <p>
     * Function<TemplateModel,Map<String,Object>> toFreeMarker -> x -> {
     * // hard-coded mapping?? tedious but offers precise control
     * }
     * <p>
     * bookshelf().buildCollector().collect().etc()
     * collector.collect().filter(keepThese).stream().forEach(template -> {
     *     var content = render(it, templateMetaModel)
     *     handler.write(content)
     * }
     * </code>
     * A Template needs to tell us:
     * a) the file source of the template mark-up
     * b) the file destination of the rendered template
     */
    @SuppressWarnings("java:S1135") // ignore TODO blocks for now
    public int generateCode(RestProjectDescriptor descriptor) {
        log.debug("generateCode: restProjectDescriptor: {}", descriptor);

        // Build the TemplateModel consumed by Freemarker to resolve template variables
        var templateModel = RestProjectTemplateModelFactory.create()
                .usingDependencyCatalog(dependencyCatalog)
                .usingProjectDescriptor(descriptor)
                .build();

        templateModel.setCustomProperties(CustomPropertyAssembler.assembleCustomProperties(archetypeDescriptorFactory,
                descriptor.getBasePackage()));

        // Create a predicate to determine which template's to render
        Predicate<CatalogEntry> keepThese = descriptor2predicate.convert(descriptor);

        // Is there a better way? I would really like the generator to be agnostic of this approach.
        // This method maps the metadata in the templateModel into a Map<TokenName,TokenValue>
        // to enable resolving mustache expressions, which have the form `{{tokenName}}`.
        mustacheDecoder.configure(templateModel);

        // Render the templates
        collector.prepare(descriptor).collect().stream().filter(keepThese).forEach(catalogEntry -> {

            // essentially: forEach: aTemplate -> { writeIt ( renderIt(aTemplate) ) }
            catalogEntry.getFacets().forEach(facet -> {
                String renderedContent = templateRenderer.render(facet.getSourceTemplate(), templateModel);

                if (catalogEntry.getArchetype() == null) {
                    // this may be dead code. need to see if error still occurs that lands us here
                    log.error("Detected a catalog entry with a null archetype: {}", catalogEntry);
                }
                else {
                    String outputFileName = OutputFileDestinationResolver.resolveDestination(
                            facet,
                            catalogEntry.getArchetype(),
                            templateModel,
                            mustacheDecoder);
                    log.info("Resolved destination: archetype: {}, destination: {}", catalogEntry.getArchetype(), outputFileName);
                    //String outputFileName = mustacheDecoder.decode(facet.getDestination());
                    outputHandler.writeOutput(outputFileName, renderedContent);
                }
            });
        });

        return ExitCodes.OK;
    }
}
