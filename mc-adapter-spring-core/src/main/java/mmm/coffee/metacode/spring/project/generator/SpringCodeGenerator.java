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
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.ExitCodes;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.dependency.DependencyCatalog;
import mmm.coffee.metacode.common.descriptor.RestProjectDescriptor;
import mmm.coffee.metacode.common.generator.ICodeGenerator;
import mmm.coffee.metacode.common.io.MetaPropertiesHandler;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.stereotype.Collector;
import mmm.coffee.metacode.common.stereotype.MetaTemplateModel;
import mmm.coffee.metacode.common.stereotype.TemplateResolver;
import mmm.coffee.metacode.common.toml.PackageDataDictionary;
import mmm.coffee.metacode.common.trait.ConvertTrait;
import mmm.coffee.metacode.common.trait.WriteOutputTrait;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModelFactory;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;

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
public class SpringCodeGenerator implements ICodeGenerator<RestProjectDescriptor> {

    private final Collector collector;
    private final ConvertTrait<RestProjectDescriptor, RestProjectTemplateModel> descriptor2templateModel;
    private final ConvertTrait<RestProjectDescriptor, Predicate<CatalogEntry>> descriptor2predicate;
    private final TemplateResolver<MetaTemplateModel> templateRenderer;
    private final WriteOutputTrait outputHandler;
    private final DependencyCatalog dependencyCatalog;
    private final MustacheDecoder mustacheDecoder;
    private final MetaPropertiesHandler<RestProjectDescriptor> metaPropertiesHandler;
    private final PackageDataDictionary dataDictionary;

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
    public SpringCodeGenerator doPreprocessing(RestProjectDescriptor descriptor) {
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
     * Function<TemplateModel,Map<String,Object> toFreeMarker -> x -> {
     * // hard-coded mapping?? tedious but offers precise control
     * }
     * <p>
     * bookshelf().buildCollector().collect().etc()
     * collector.collect().filter(keepThese).stream().forEach(template -> {
     * var content = render(it, templateMetaModel)
     * handler.write(content)
     * }
     * </code>
     * A Template needs to tell us:
     * a) the file source of the template mark-up
     * b) the file destination of the rendered template
     */
    @SuppressWarnings("java:S1135") // ignore TODO blocks for now
    public int generateCode(RestProjectDescriptor descriptor) {
        log.debug("generateCode: descriptor: {}", descriptor);

        if (log.isInfoEnabled()) {
            for (String cname : collector.catalogs()) {
                log.info("[generateCode]: candidate catalog: {}", cname);
            }
        }

        // Build the TemplateModel consumed by Freemarker to resolve template variables
        var templateModel = RestProjectTemplateModelFactory.create()
                .usingDependencyCatalog(dependencyCatalog)
                .usingProjectDescriptor(descriptor)
                .usingDataDictionary(dataDictionary)
                .build();

        // Create a predicate to determine which template's to render
        Predicate<CatalogEntry> keepThese = descriptor2predicate.convert(descriptor);

        // Is there a better way? I would really like the generator to be agnostic of this approach.
        // This method maps the metadata in the templateModel into a Map<TokenName,TokenValue>
        // to enable resolving mustache expressions, which have the form `{{tokenName}}`.
        mustacheDecoder.configure(templateModel);

        // Render the templates
        collector.prepare(descriptor).collect().stream().filter(keepThese).forEach(catalogEntry -> {
            log.debug("Processing the catalogEntry having sourceTemplate: {}", catalogEntry.getFacets().get(0).getSourceTemplate());

            Archetype archetype = catalogEntry.archetypeValue();
            String packageName = dataDictionary.packageName(archetype);
            String canonicalClassName = dataDictionary.canonicalClassNameOf(archetype);
            log.info("Archetype: {}, packageName: {}, className:", archetype, packageName, canonicalClassName);

            // essentially: aTemplate -> { writeIt ( renderIt(aTemplate) ) }
            catalogEntry.getFacets().forEach(facet -> {
                String renderedContent = templateRenderer.render(facet.getSourceTemplate(), templateModel);
                // Destinations may have mustache expressions that need to be decoded
                String outputFileName = mustacheDecoder.decode(facet.getDestination());
                outputHandler.writeOutput(outputFileName, renderedContent);
            });
        });

        return ExitCodes.OK;
    }
}
