package mmm.coffee.metacode.spring.project.generator;

import com.samskivert.mustache.MustacheException;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.common.rule.PackageNameConversion;
import mmm.coffee.metacode.common.trait.DecodeTrait;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;
import mmm.coffee.metacode.spring.project.mustache.MustacheDecoder;

import java.util.Map;

@Slf4j
public class OutputFileDestinationResolver {

    public static String resolveDestination(TemplateFacet facet,
                                             String archetypeName,
                                             SpringTemplateModel templateModel,
                                             DecodeTrait decoder)
    {
        log.info("[resolveDestination] archetypeName: {}, facet: {}", archetypeName, facet);
        String destinationExpression = facet.getDestination();
        Map<String,Object> customProps = templateModel.getCustomProperties();
        if (customProps != null) {
            Object descriptor = customProps.get(archetypeName);
            if (descriptor instanceof JavaArchetypeDescriptor) {
                JavaArchetypeDescriptor jad = (JavaArchetypeDescriptor) descriptor;
                switch (jad.archetype()) {
                    case Text, Undefined -> {
                        // This block handles text files, like build.gradle, settings.gradle, application.yml
                        return decoder.decode(destinationExpression);
                    }
                    default -> {
                        log.info("[resolveDestination] jad: {}", jad);
                        String facetName = facet.getFacet();
                        String sourceFileName = "";
                        switch (facetName) {
                            case "test" -> {
                                sourceFileName = "/src/test/java/" + PackageNameConversion.toFqFileName(jad.fqcnUnitTest());
                            }
                            case "integrationTest" -> {
                                sourceFileName = "/src/integrationTest/java/" + PackageNameConversion.toFqFileName(jad.fqcnIntegrationTest());
                            }
                            case "main" -> {
                                sourceFileName = "/src/main/java/" + PackageNameConversion.toFqFileName(jad.fqcn());
                            }
                            case "testFixtures" -> {
                                sourceFileName = "/src/testFixtures/java/" + PackageNameConversion.toFqFileName(jad.fqcnTestFixture());
                            }
                            default -> {
                                String msg = String.format("Encountered an unknown facet '%s' for archetype %s", facetName, archetypeName);
                                log.error(msg);
                                throw new RuntimeException(msg);
                            }
                        }
                        return "application" + sourceFileName;
                    }
                }
            }
        }
        // If customProps is null we land here
        try {
            return decoder.decode(destinationExpression);
        }
        catch (MustacheException | NullPointerException e) {
            log.error("Unable to determine the file destination for archetype: {}, facet: {}", archetypeName, facet, e);
            throw e;
        }
    }

}
