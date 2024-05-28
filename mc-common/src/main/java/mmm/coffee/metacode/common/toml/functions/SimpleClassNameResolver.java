package mmm.coffee.metacode.common.toml.functions;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.model.Archetype;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SimpleClassNameResolver {
    private static final SimpleClassNameResolver INSTANCE = new SimpleClassNameResolver();

    private static final Map<Archetype, String> patterns = new EnumMap<>(Archetype.class);

    static {
        // TODO: Can these be loaded from a config file? for example:
        // [class-naming-strategy.toml]
        // Controller = "%sController"
        // CustomRepository = "%sRepository"
        patterns.put(Archetype.Controller, "%sController");                  // eg: PetController
        patterns.put(Archetype.CustomRepository, "%sRepository");            // eg: PetRepository
        patterns.put(Archetype.DataStoreApi, "%sDataStore");                 // eg: PetDataStore
        patterns.put(Archetype.DataStoreImpl, "%sDataStoreProvider");        // eg: PetDataStoreProvider
        patterns.put(Archetype.Entity, "%sEntity");                          // eg: PetEntity
        patterns.put(Archetype.EntityToPojoConverter, "%sEntityToPojoConverter");     // eg: PetEntityToPojoConverter
        patterns.put(Archetype.PojoToEntityConverter, "%sPojoToEntityConverter");     // eg: PetPojoToEntityConverter
        patterns.put(Archetype.Repository, "%sRepository");                  // eg: PetRepository
        patterns.put(Archetype.ResourcePojo, "%s");                          // eg: Pet (or maybe someday PetProjection or PetView)
        patterns.put(Archetype.Routes, "%sRoutes");                          // eg: PetRoutes
        patterns.put(Archetype.ServiceApi, "%sService");                     // eg: PetService
        patterns.put(Archetype.ServiceImpl, "%sServiceProvider");            // eg: PetServiceProvider
    }

    public static String simpleClassName(String resource, @Nonnull Archetype key) {
        Objects.requireNonNull(key);
        return INSTANCE.toSimpleClassName(resource, key);
    }

    public static String simpleClassName(@Nonnull Archetype key) {
        Objects.requireNonNull(key);
        return INSTANCE.toSimpleClassName(null, key);
    }

    public static String variableName(String resource) {
        String var = INSTANCE.toSimpleClassName(resource, Archetype.ResourcePojo);
        String firstLetter = var.substring(0, 1).toLowerCase();
        return firstLetter + var.substring(1);
    }

    public String toSimpleClassName(String resource, @Nonnull Archetype classKey) {
        Objects.requireNonNull(classKey, String.format("No archetype provided for resource '%s'", resource));

        String pattern = patterns.get(classKey);
        if (pattern == null) {
            // Project-level classes may not have a pattern. For instance, the WebMvcConfiguration class
            // or the GlobalExceptionHandler class, whose names aren't dependent on a resource name.
            return classKey.name();
        } else if (resource != null) {
            return String.format(pattern, resource);
        } else {
            log.debug("[toSimpleClassName] No pattern was found for prototype class: {}", classKey.name());
            return classKey.name();
        }
    }

}
