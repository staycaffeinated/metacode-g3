package mmm.coffee.metacode.spring.project.generator;

import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.catalog.TemplateFacet;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.common.rule.PackageNameConversion;
import mmm.coffee.metacode.common.trait.DecodeTrait;
import mmm.coffee.metacode.spring.project.model.SpringTemplateModel;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class OutputFileDestinationResolver {

    private OutputFileDestinationResolver() {
        // essentially sealed
    }

    @SuppressWarnings("java:S1301") // I don't want to rewrite these switch stmts into an if stmts
    public static String resolveDestination(TemplateFacet facet,
                                            String archetypeName,
                                            SpringTemplateModel templateModel,
                                            DecodeTrait decoder) {
        log.info("[resolveDestination] archetypeName: {}, facet: {}", archetypeName, facet);
        String destinationExpression = facet.getDestination();
        Map<String, Object> customProps = templateModel.getCustomProperties();
        if (customProps != null) {
            Object descriptor = customProps.get(archetypeName);
            if (descriptor instanceof JavaArchetypeDescriptor jad) {
                switch (jad.archetype()) {
                    case Text, Undefined -> {
                        // This block handles text files, like build.gradle, settings.gradle, application.yml
                        return decoder.decode(destinationExpression);
                    }
                    default -> {
                        log.info("[resolveDestination] jad: {}", jad);
                        String facetName = facet.getFacet();
                        if (facetName == null) {
                            log.error("A catalog entry was encountered that did not have a facet name.");
                            log.error("The error was encountered on this sourceTemplate: {}", facet.getSourceTemplate());
                            log.error("This most likely is a spelling error in the catalog (eg, 'facet' is misspelled");
                        }
                        Objects.requireNonNull(facetName, "A null facet name was encountered in the OutputFileDestinationResolver");
                        String sourceFileName = switch(facetName) {
                            case "test" -> "/src/test/java/" + PackageNameConversion.toFqFileName(jad.fqcnUnitTest());
                            case "integrationTest" ->
                                    "/src/integrationTest/java/" + PackageNameConversion.toFqFileName(jad.fqcnIntegrationTest());
                            case "main" ->
                                    "/src/main/java/" + PackageNameConversion.toFqFileName(jad.fqcn());
                            case "testFixtures" ->
                                    "/src/testFixtures/java/" + PackageNameConversion.toFqFileName(jad.fqcnTestFixture());
                            default -> {
                                String msg = String.format("Encountered an unknown facet '%s' for archetype %s", facetName, archetypeName);
                                log.error(msg);
                                throw new RuntimeApplicationError(msg);
                            }
                        };
                        // we want the application module at the beginning of the path,
                        // so something like: `application/src/main/java/org/example/Application.java'
                        // or `application/src/test/java/org/example/ApplicationTest.java'
                        return "application" + sourceFileName;
                    }
                }
            }
        }
        // If customProps is null we land here
        log.error("Unable to resolve the destination of {}", archetypeName);
        log.error("If this block is reached when processing a project-scope archetype, it is possible%n the archetype is not included in the ProjectArchetypeToMap class,%n so the archetype is mistaken for an endpoint-scope archetype.");
        log.debug("[resolveDestination] destinationExpression: {}", destinationExpression);
        log.debug("[resolveDestination] archetypeName: {}", archetypeName);
        return null;

    }

}
