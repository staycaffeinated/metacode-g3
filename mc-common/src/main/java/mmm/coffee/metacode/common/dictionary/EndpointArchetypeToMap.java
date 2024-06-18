package mmm.coffee.metacode.common.dictionary;

import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.arch.Processor;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import static org.apache.commons.lang3.ArrayUtils.contains;

/**
 * This produces a map containing each project-scope Archetype and the matching archetype descriptor.
 * This map can be passed to the template renderer to enable the template render to look up
 * information about the matching java class (each archetype matches to the rendering of a Java class
 * or other artifact, such as a `build.gradle` file.).
 */
public class EndpointArchetypeToMap {

    private static final Archetype[] ENDPOINT_ARCHETYPES = computeEndpointArchetypes();

    /**
     * Returns a map of each project-scope archetype and that archetype's descriptor.
     * This map is added to the template model that's applied when rendering templates.
     * This mapping enables templates to use expressions like `ProblemConfiguration.fqcn()`,
     * which enables the namespace of any class to be configurable instead of being hard-coded.
     *
     * @param factory this will be interrogated to acquire the descriptor information
     * @param restResource the RESTful resource (eg, Pet, Store, Owner...)
     * @return a mapping of archetype.name() and the matching descriptor
     */
    public static Map<String, ArchetypeDescriptor> map(@NonNull IArchetypeDescriptorFactory factory, @NonNull String restResource) {
        Map<String, ArchetypeDescriptor> results = new TreeMap<>();
        for (Archetype archetype : ProjectArchetypeToMap.PROJECT_ARCHETYPES) {
            results.put(archetype.name(), factory.createArchetypeDescriptor(archetype));
        }

        for (Archetype archetype : ENDPOINT_ARCHETYPES) {
            results.put(archetype.name(), factory.createArchetypeDescriptor(archetype, restResource));
        }

        return results;
    }

    /**
     * Calculate the endpoint-scoped archetypes
     */
    public static Archetype[] computeEndpointArchetypes() {
        Archetype[] allValues = Archetype.values();
        Archetype[] projectValues = ProjectArchetypeToMap.PROJECT_ARCHETYPES;

        // Subtract the project-scoped archetypes from the set of all archetypes.
        // Whatever is left are the endpoint-scoped archetypes
        for (Archetype item : projectValues) {
            ArrayUtils.removeAllOccurrences(allValues, item);
        }
        return allValues;
    }

}
