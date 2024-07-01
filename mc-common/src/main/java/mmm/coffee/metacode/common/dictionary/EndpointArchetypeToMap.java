package mmm.coffee.metacode.common.dictionary;

import com.samskivert.mustache.MustacheException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;

import java.util.*;

/**
 * This produces a map containing each project-scope Archetype and the matching archetype descriptor.
 * This map can be passed to the template renderer to enable the template render to look up
 * information about the matching java class (each archetype matches to the rendering of a Java class
 * or other artifact, such as a `build.gradle` file.).
 */
@Slf4j
public class EndpointArchetypeToMap {

    public static final Archetype[] ENDPOINT_ARCHETYPES = computeEndpointArchetypes();

    /**
     * Returns a map of each project-scope archetype and that archetype's descriptor.
     * This map is added to the template model that's applied when rendering templates.
     * This mapping enables templates to use expressions like `ProblemConfiguration.fqcn()`,
     * which enables the namespace of any class to be configurable instead of being hard-coded.
     *
     * @param factory      this will be interrogated to acquire the descriptor information
     * @param restResource the RESTful resource (eg, Pet, Store, Owner...)
     * @return a mapping of archetype.name() and the matching descriptor
     */
    public static Map<String, ArchetypeDescriptor> map(@NonNull IArchetypeDescriptorFactory factory, @NonNull String restResource) {
        log.debug("[map] factory is-a {}", factory.getClass().getSimpleName());
        try {
            Map<String, ArchetypeDescriptor> results = new TreeMap<>();
            for (Archetype archetype : ProjectArchetypeToMap.PROJECT_ARCHETYPES) {
                results.put(archetype.name(), factory.createArchetypeDescriptor(archetype));
            }

            for (Archetype archetype : ENDPOINT_ARCHETYPES) {
                log.debug("Creating endpoint ArchetypeDescriptor for {}", archetype);
                JavaArchetypeDescriptor descriptor = factory.createArchetypeDescriptor(archetype, restResource);
                log.debug("Descriptor = {}", descriptor);
                results.put(archetype.name(), factory.createArchetypeDescriptor(archetype, restResource));
            }

            return results;
        } catch (MustacheException e) {
            log.error("Unable to map: {}", restResource);
            throw e;
        }
    }

    /**
     * Calculate the endpoint-scoped archetypes
     */
    public static Archetype[] computeEndpointArchetypes() {
        Archetype[] projectValues = ProjectArchetypeToMap.PROJECT_ARCHETYPES;
        List<Archetype> endpointList = new ArrayList<Archetype>(EnumSet.allOf(Archetype.class));

        // Subtract the project-scoped archetypes from the set of all archetypes.
        // Whatever is left are the endpoint-scoped archetypes
        for (Archetype item : projectValues) {
            endpointList.remove(item);
        }
        Archetype[] endpointValues = new Archetype[endpointList.size()];
        int i = 0;
        for (Archetype t : endpointList) {
            endpointValues[i++] = t;
        }
        return endpointValues;
    }

}
