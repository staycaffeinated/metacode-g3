package mmm.coffee.metacode.common.toml.functions;

import mmm.coffee.metacode.common.toml.ClassKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

public class SimpleClassNameResolverTests {

    SimpleClassNameResolver classUnderTest = new SimpleClassNameResolver();

    private static final String CONTROLLER = ClassKey.Controller.name();
    private static final String CUSTOM_REPOSITORY = ClassKey.CustomRepository.name();
    private static final String DATA_STORE_API = ClassKey.DataStoreApi.name();
    private static final String DATA_STORE_IMPL = ClassKey.DataStoreImpl.name();
    private static final String ENTITY = ClassKey.Entity.name();
    private static final String ENTITY_TO_POJO_CONVERTER = ClassKey.EntityToPojoConverter.name();

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetController",
            "Owner, OwnerController",
            "Store, StoreController"
    })
    void shouldReturnSimpleNameOfController(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.Controller);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetRepository",
            "Owner, OwnerRepository",
            "Store, StoreRepository"
    })
    void shouldReturnSimpleNameOfCustomRepository(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.CustomRepository);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetDataStore",
            "Owner, OwnerDataStore",
            "Store, StoreDataStore"
    })
    void shouldReturnSimpleNameOfDataStoreApi(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.DataStoreApi);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetDataStoreProvider",
            "Owner, OwnerDataStoreProvider",
            "Store, StoreDataStoreProvider"
    })
    void shouldReturnSimpleNameOfDataStoreImpl(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.DataStoreImpl);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetEntity",
            "Owner, OwnerEntity",
            "Store, StoreEntity"
    })
    void shouldReturnSimpleNameOfEntity(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.Entity);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetEntityToPojoConverter",
            "Owner, OwnerEntityToPojoConverter",
            "Store, StoreEntityToPojoConverter"
    })
    void shouldReturnSimpleNameOfEntityToPojo(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.EntityToPojoConverter);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetPojoToEntityConverter",
            "Owner, OwnerPojoToEntityConverter",
            "Store, StorePojoToEntityConverter"
    })
    void shouldReturnSimpleNameOfPojoToEntity(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.PojoToEntityConverter);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetRepository",
            "Owner, OwnerRepository",
            "Store, StoreRepository"
    })
    void shouldReturnSimpleNameOfRepository(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.Repository);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, Pet",
            "Owner, Owner",
            "Store, Store"
    })
    void shouldReturnSimpleNameOfResourcePojo(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.ResourcePojo);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetRoutes",
            "Owner, OwnerRoutes",
            "Store, StoreRoutes"
    })
    void shouldReturnSimpleNameOfRoutes(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.Routes);
        assertThat(actual).isEqualTo(expected);
    }
    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetService",
            "Owner, OwnerService",
            "Store, StoreService"
    })
    void shouldReturnSimpleNameOfServiceApi(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.ServiceApi);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Pet, PetServiceProvider",
            "Owner, OwnerServiceProvider",
            "Store, StoreServiceProvider"
    })
    void shouldReturnSimpleNameOfServiceImpl(String resource, String expected) {
        String actual = classUnderTest.toSimpleClassName(resource, ClassKey.ServiceImpl);
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void shouldReturnSimpleNameOfBuiltinClasses() {
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.AlphabeticField)).isEqualTo("AlphabeticField");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.AlphabeticValidator)).isEqualTo("AlphabeticValidator");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.BadRequestException)).isEqualTo("BadRequestException");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.DateTimeFormatConfiguration)).isEqualTo("DateTimeFormatConfiguration");

        assertThat(classUnderTest.toSimpleClassName("", ClassKey.UnprocessableEntityException)).isEqualTo("UnprocessableEntityException");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.BadRequestException)).isEqualTo("BadRequestException");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.GenericDataStore)).isEqualTo("GenericDataStore");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.GlobalExceptionHandler)).isEqualTo("GlobalExceptionHandler");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.ProblemConfiguration)).isEqualTo("ProblemConfiguration");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.SecureRandomSeries)).isEqualTo("SecureRandomSeries");
        assertThat(classUnderTest.toSimpleClassName("", ClassKey.SpringProfiles)).isEqualTo("SpringProfiles");

    }

}