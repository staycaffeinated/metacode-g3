package mmm.coffee.metacode.spring.model;

import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import org.springframework.stereotype.Component;

@Component
public class ClassInventoryFactory {

    private final PackageLayoutRuleSet packageLayoutRuleSet;
    private final ClassNameRuleSet classNameRuleSet;

    public ClassInventoryFactory(PackageLayoutRuleSet packageLayoutRuleSet, ClassNameRuleSet classNameRuleSet) {
        this.packageLayoutRuleSet = packageLayoutRuleSet;
        this.classNameRuleSet = classNameRuleSet;
    }


    /**
     * Use this for project-scope, when a resource name is unknown
     *
     * @return the ClassInventory, which is later passed into the template engine
     */
    public static ClassInventory create() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static ClassInventory create(String resourceName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    @SuppressWarnings({"java:S108"})
    protected static void assignValue(ClassInventory classInventory, Archetype archetype) {

        /*
        switch (archetype) {
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

         */


    }

}
