package mmm.coffee.metacode.common.toml;

/**
 * These are keys that can be assigned a value in the package schema TOML file
 * to customize the package names assigned to classes, and the names of the classes.
 */
@SuppressWarnings("java:S115")  // the camel-case naming convention is by-design
public enum PrototypeClass {
    ServiceApi,
    ServiceImpl,
    Controller,
    Routes,
    ResourcePojo,
    DataStoreApi,
    DataStoreImpl,
    Entity,
    Repository,
    EntityToPojoConverter,
    PojoToEntityConverter,
    GlobalExceptionHandler,
    ApplicationConfiguration,
    DateTimeFormatConfiguration,
    ProblemConfiguration,
    LocalDateConverter,
    WebMvcConfiguration,
    SpringProfiles,
    ResourceIdTrait,
    AlphabeticField,
    AlphabeticValidator,
    ResourceIdField,
    ResourceIdValidator,
    SecureRandomSeries,
    BadRequestException,
    ResourceNotFoundException,
    UnprocessableEntityException,
    CustomRepository,
    GenericDataStore,

    UNDEFINED;
    // For values that don't map to a known key.
    // When a classKey is encountered that doesn't map to a known value,
    // the generator will write that class (if possible) into the ".misc" package to
    // make those class keys missing from this catalog easier to discover.
    // For instance: if a template catalog uses a classKey that isn't found here.

}
