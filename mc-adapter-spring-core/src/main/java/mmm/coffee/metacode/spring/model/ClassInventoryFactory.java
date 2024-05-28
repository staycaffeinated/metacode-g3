package mmm.coffee.metacode.spring.model;

import mmm.coffee.metacode.common.toml.PackageDataDictionary;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.toml.functions.SimpleClassNameResolver;

public class ClassInventoryFactory {

    private ClassInventoryFactory() {
        // should not be instantiated outside of this class
    }

    /**
     * Use this for project-scope, when a resource name is unknown
     *
     * @param dictionary contains the package schema to apply; query this object to discover the
     *                   name of the package a class belongs to. With a package schema, an end-user
     *                   can declare whether, for instance, the Controller class goes into
     *                   `org.example.petstore.api` or `org.example.petstore.endpoint.pet` or
     *                   whatever package the end-user chooses.
     * @return the ClassInventory, which is later passed into the template engine
     */
    public static ClassInventory create(PackageDataDictionary dictionary) {
        return create(dictionary, null);
    }

    public static ClassInventory create(PackageDataDictionary dictionary, String resourceName) {
        ClassInventory classInventory = new ClassInventory();

        Archetype[] protoClasses = Archetype.values();
        for (Archetype protoClass : protoClasses) {
            assignValue(classInventory, protoClass, dictionary, resourceName);
        }
        return classInventory;
    }


    @SuppressWarnings({"java:S108"})
    protected static void assignValue(ClassInventory classInventory, Archetype prototypeClass, PackageDataDictionary dictionary, String resourceName) {
        ClassInfo classInfo = new ClassInfo(
                SimpleClassNameResolver.simpleClassName(resourceName, prototypeClass),
                dictionary.packageName(prototypeClass),
                dictionary.canonicalClassNameOf(resourceName, prototypeClass),
                SimpleClassNameResolver.variableName(resourceName));

        switch (prototypeClass) {
            case Entity -> classInventory.setEntity(classInfo);
            case Routes -> classInventory.setRoutes(classInfo);
            case Controller -> classInventory.setController(classInfo);
            case Repository -> classInventory.setRepository(classInfo);
            case ServiceApi -> classInventory.setServiceApi(classInfo);
            case ServiceImpl -> classInventory.setServiceImpl(classInfo);
            case DataStoreApi -> classInventory.setDataStoreApi(classInfo);
            case ResourcePojo -> classInventory.setResourcePojo(classInfo);
            case DataStoreImpl -> classInventory.setDataStoreImpl(classInfo);
            case SpringProfiles -> classInventory.setSpringProfiles(classInfo);
            case AlphabeticAnnotation -> classInventory.setAlphabeticAnnotation(classInfo);
            case ResourceIdAnnotation -> classInventory.setResourceIdAnnotation(classInfo);
            case ResourceIdTrait -> classInventory.setResourceIdTrait(classInfo);
            case CustomRepository -> classInventory.setCustomRepository(classInfo);
            case GenericDataStore -> classInventory.setGenericDataStore(classInfo);
            case LocalDateConverter -> classInventory.setLocalDateConverter(classInfo);
            case SecureRandomSeries -> classInventory.setSecureRandomSeries(classInfo);
            case AlphabeticValidator -> classInventory.setAlphabeticValidator(classInfo);
            case BadRequestException -> classInventory.setBadRequestException(classInfo);
            case ResourceIdValidator -> classInventory.setResourceIdValidator(classInfo);
            case WebMvcConfiguration -> classInventory.setWebMvcConfiguration(classInfo);
            case ProblemConfiguration -> classInventory.setProblemConfiguration(classInfo);
            case EntityToPojoConverter -> classInventory.setEntityToPojoConverter(classInfo);
            case PojoToEntityConverter -> classInventory.setPojoToEntityConverter(classInfo);
            case GlobalExceptionHandler -> classInventory.setGlobalExceptionHandler(classInfo);
            case ApplicationConfiguration -> classInventory.setApplicationConfiguration(classInfo);
            case ResourceNotFoundException -> classInventory.setResourceNotFoundException(classInfo);
            case DateTimeFormatConfiguration -> classInventory.setDateTimeFormatConfiguration(classInfo);
            case UnprocessableEntityException -> classInventory.setUnprocessableEntityException(classInfo);
            case Undefined -> {
            }
        }


    }

}
