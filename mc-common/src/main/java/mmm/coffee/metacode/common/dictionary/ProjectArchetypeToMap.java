package mmm.coffee.metacode.common.dictionary;

import lombok.NonNull;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;

import java.util.Map;
import java.util.TreeMap;

/**
 * This produces a map containing each project-scope Archetype and the matching archetype descriptor.
 * This map can be passed to the template renderer to enable the template render to look up
 * information about the matching java class (each archetype matches to the rendering of a Java class
 * or other artifact, such as a `build.gradle` file.).
 */
public class ProjectArchetypeToMap {

    private ProjectArchetypeToMap() {
    }

    protected static final Archetype[] PROJECT_ARCHETYPES = {
            // application class
            Archetype.Application,
            Archetype.RootController,
            Archetype.RootControllerExceptionHandler,
            Archetype.RootService,
            Archetype.DefaultController,
            Archetype.DefaultService,

            // validation/annotation/stereotypes
            Archetype.AlphabeticAnnotation,
            Archetype.AlphabeticValidator,
            Archetype.OnCreateAnnotation,
            Archetype.OnUpdateAnnotation,
            Archetype.ResourceIdAnnotation,
            Archetype.SearchTextAnnotation,
            Archetype.ResourceIdSupplier,
            Archetype.ResourceIdTrait,
            Archetype.ResourceIdValidator,
            Archetype.SearchTextValidator,
            Archetype.SecureRandomSeries,
            Archetype.SpringProfiles,
            Archetype.ResourceIdentity,
            Archetype.UpdateAwareConverter,

            // exception stuff
            Archetype.GlobalExceptionHandler,
            Archetype.Exception,
            Archetype.ResourceNotFoundException,
            Archetype.UnprocessableEntityException,
            Archetype.BadRequestException,
            Archetype.ProblemSummary,

            // config stuff
            Archetype.LocalDateConverter,
            Archetype.ProblemConfiguration,
            Archetype.WebMvcConfiguration,
            Archetype.WebFluxConfiguration,
            Archetype.ApplicationConfiguration,
            Archetype.ContainerConfiguration,
            Archetype.DateTimeFormatConfiguration,
            Archetype.BatchConfiguration,
            Archetype.JobCompletionNotificationListener,

            // persistence stuff
            Archetype.AbstractDataJpaTest,
            Archetype.CustomRepository,     // the base interface extended by specific repos; eg: PetRepository extends CustomRepository
            Archetype.CustomSQLRepository,
            Archetype.DataStoreApi,
            Archetype.GenericDataStore,
            Archetype.RegisterDatabaseProperties,
            Archetype.MongoRegisterDatabaseProperties,
            Archetype.ContainerConfiguration,
            Archetype.DatabaseInitFunction,
            Archetype.MongoDatabaseConfiguration,
            Archetype.TestDatabaseConfiguration,
            Archetype.PostgresDbContainerTests,

            // miscellaneous
            Archetype.Text,
            Archetype.AbstractIntegrationTest,
            Archetype.Undefined
    };

    /**
     * Returns a map of each project-scope archetype and that archetype's descriptor.
     * This map is added to the template model that's applied when rendering templates.
     * This mapping enables templates to use expressions like `ProblemConfiguration.fqcn()`,
     * which enables the namespace of any class to be configurable instead of being hard-coded.
     *
     * @param factory this will be interrogated to acquire the descriptor information
     * @return a mapping of archetype.name() and the matching descriptor
     */
    public static Map<String, ArchetypeDescriptor> map(@NonNull IArchetypeDescriptorFactory factory) {
        Map<String, ArchetypeDescriptor> results = new TreeMap<>();
        for (Archetype archetype : PROJECT_ARCHETYPES) {
            results.put(archetype.name(), factory.createArchetypeDescriptor(archetype));
        }
        return results;
    }

}
