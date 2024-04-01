package mmm.coffee.metacode.common.toml.functions;

import jakarta.annotation.Nonnull;
import mmm.coffee.metacode.common.toml.ClassKey;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class SimpleClassNameResolver {
    private static final SimpleClassNameResolver INSTANCE = new SimpleClassNameResolver();

    private static final Map<ClassKey, String> patterns = new EnumMap<>(ClassKey.class);

    static {
        // TODO: Can these be loaded from a config file? for example:
        // [class-naming-strategy]
        // Controller = "%sController"
        // CustomRepository = "%sRepository"
        patterns.put(ClassKey.Controller, "%sController");                  // eg: PetController
        patterns.put(ClassKey.CustomRepository, "%sRepository");            // eg: PetRepository
        patterns.put(ClassKey.DataStoreApi, "%sDataStore");                 // eg: PetDataStore
        patterns.put(ClassKey.DataStoreImpl, "%sDataStoreProvider");        // eg: PetDataStoreProvider
        patterns.put(ClassKey.Entity, "%sEntity");                          // eg: PetEntity
        patterns.put(ClassKey.EntityToPojoConverter, "%sEntityToPojoConverter");     // eg: PetEntityToPojoConverter
        patterns.put(ClassKey.PojoToEntityConverter, "%sPojoToEntityConverter");     // eg: PetPojoToEntityConverter
        patterns.put(ClassKey.Repository, "%sRepository");                  // eg: PetRepository
        patterns.put(ClassKey.ResourcePojo, "%s");                          // eg: Pet
        patterns.put(ClassKey.Routes, "%sRoutes");                          // eg: PetRoutes
        patterns.put(ClassKey.ServiceApi, "%sService");                     // eg: PetService
        patterns.put(ClassKey.ServiceImpl, "%sServiceProvider");            // eg: PetServiceProvider
    }

    public String toSimpleClassName(@Nonnull String resource, @Nonnull ClassKey classKey) {
        Objects.requireNonNull(resource);
        Objects.requireNonNull(classKey);

        String pattern = patterns.get(classKey);
        if (pattern == null) {
            return classKey.name();
        } else
            return String.format(pattern, resource);

    }

    public static String simpleClassName(String resource, ClassKey key) {
        return INSTANCE.toSimpleClassName(resource, key);
    }


}
