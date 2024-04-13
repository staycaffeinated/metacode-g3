package mmm.coffee.metacode.common.toml.functions;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.toml.PrototypeClass;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SimpleClassNameResolver {
    private static final SimpleClassNameResolver INSTANCE = new SimpleClassNameResolver();

    private static final Map<PrototypeClass, String> patterns = new EnumMap<>(PrototypeClass.class);

    static {
        // TODO: Can these be loaded from a config file? for example:
        // [class-naming-strategy.toml]
        // Controller = "%sController"
        // CustomRepository = "%sRepository"
        patterns.put(PrototypeClass.Controller, "%sController");                  // eg: PetController
        patterns.put(PrototypeClass.CustomRepository, "%sRepository");            // eg: PetRepository
        patterns.put(PrototypeClass.DataStoreApi, "%sDataStore");                 // eg: PetDataStore
        patterns.put(PrototypeClass.DataStoreImpl, "%sDataStoreProvider");        // eg: PetDataStoreProvider
        patterns.put(PrototypeClass.Entity, "%sEntity");                          // eg: PetEntity
        patterns.put(PrototypeClass.EntityToPojoConverter, "%sEntityToPojoConverter");     // eg: PetEntityToPojoConverter
        patterns.put(PrototypeClass.PojoToEntityConverter, "%sPojoToEntityConverter");     // eg: PetPojoToEntityConverter
        patterns.put(PrototypeClass.Repository, "%sRepository");                  // eg: PetRepository
        patterns.put(PrototypeClass.ResourcePojo, "%s");                          // eg: Pet (or maybe someday PetProjection or PetView)
        patterns.put(PrototypeClass.Routes, "%sRoutes");                          // eg: PetRoutes
        patterns.put(PrototypeClass.ServiceApi, "%sService");                     // eg: PetService
        patterns.put(PrototypeClass.ServiceImpl, "%sServiceProvider");            // eg: PetServiceProvider
    }

    public static String simpleClassName(String resource, PrototypeClass key) {
        return INSTANCE.toSimpleClassName(resource, key);
    }

    public static String simpleClassName(PrototypeClass key) {
        return INSTANCE.toSimpleClassName(null, key);
    }

    public static String variableName(String resource) {
        String var = INSTANCE.toSimpleClassName(resource, PrototypeClass.ResourcePojo);
        String firstLetter = var.substring(0, 1).toLowerCase();
        return firstLetter + var.substring(1);
    }

    public String toSimpleClassName(String resource, @Nonnull PrototypeClass classKey) {
        Objects.requireNonNull(classKey);

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
