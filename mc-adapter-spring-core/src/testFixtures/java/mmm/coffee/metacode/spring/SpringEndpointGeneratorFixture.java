package mmm.coffee.metacode.spring;

import mmm.coffee.metacode.common.catalog.CatalogFileReader;
import mmm.coffee.metacode.common.descriptor.Framework;
import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.freemarker.ConfigurationFactory;
import mmm.coffee.metacode.common.freemarker.FreemarkerTemplateResolver;
import mmm.coffee.metacode.common.writer.ContentToNullWriter;
import mmm.coffee.metacode.spring.catalog.SpringEndpointCatalog;
import mmm.coffee.metacode.spring.converter.NameConverter;
import mmm.coffee.metacode.spring.converter.RouteConstantsConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToPredicateConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointDescriptorToTemplateModelConverter;
import mmm.coffee.metacode.spring.endpoint.converter.RestEndpointTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.endpoint.generator.SpringEndpointGenerator;
import mmm.coffee.metacode.spring.endpoint.mustache.MustacheEndpointDecoder;

import java.io.IOException;

import static mmm.coffee.metacode.spring.ClassNameRuleSetFixture.buildClassNameRuleSet;
import static mmm.coffee.metacode.spring.MetaPropertiesHandlerFixture.buildMockMetaPropertiesHandler;
import static mmm.coffee.metacode.spring.PackageLayoutRulesetFixture.buildPackageLayoutRuleSet;

public class SpringEndpointGeneratorFixture {

    public static SpringEndpointGenerator fullOnGenerator() throws IOException {
        var converterO1 = new RestEndpointTemplateModelToMapConverter();

        return SpringEndpointGenerator.builder()
                .collector(new SpringEndpointCatalog(new CatalogFileReader()))
                .descriptor2predicate(new RestEndpointDescriptorToPredicateConverter())
                .descriptor2templateModel(new RestEndpointDescriptorToTemplateModelConverter(new NameConverter(), new RouteConstantsConverter()))
                .metaPropertiesHandler(buildMockMetaPropertiesHandler(Framework.SPRING_WEBMVC.frameworkName()))
                .mustacheDecoder(MustacheEndpointDecoder.builder()
                        .converter(converterO1)
                        .build())
                .outputHandler(new ContentToNullWriter())
                .templateRenderer(new FreemarkerTemplateResolver(ConfigurationFactory.defaultConfiguration(Constants.TEMPLATE_DIRECTORY)))
                .archetypeDescriptorFactory(new ArchetypeDescriptorFactory(buildPackageLayoutRuleSet(), buildClassNameRuleSet()))
                .build();

    }

}
