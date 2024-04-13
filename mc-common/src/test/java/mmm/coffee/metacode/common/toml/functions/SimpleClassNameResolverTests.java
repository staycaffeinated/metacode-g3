package mmm.coffee.metacode.common.toml.functions;

import mmm.coffee.metacode.common.toml.PrototypeClass;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

public class SimpleClassNameResolverTests {

    SimpleClassNameResolver classUnderTest = new SimpleClassNameResolver();
    

    @Nested
    class ToSimpleClassNameTests {

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetController",
                "Owner, OwnerController",
                "Store, StoreController"
        })
        void shouldReturnSimpleNameOfController(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.Controller);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRepository",
                "Owner, OwnerRepository",
                "Store, StoreRepository"
        })
        void shouldReturnSimpleNameOfCustomRepository(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.CustomRepository);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetDataStore",
                "Owner, OwnerDataStore",
                "Store, StoreDataStore"
        })
        void shouldReturnSimpleNameOfDataStoreApi(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.DataStoreApi);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetDataStoreProvider",
                "Owner, OwnerDataStoreProvider",
                "Store, StoreDataStoreProvider"
        })
        void shouldReturnSimpleNameOfDataStoreImpl(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.DataStoreImpl);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetEntity",
                "Owner, OwnerEntity",
                "Store, StoreEntity"
        })
        void shouldReturnSimpleNameOfEntity(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.Entity);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetEntityToPojoConverter",
                "Owner, OwnerEntityToPojoConverter",
                "Store, StoreEntityToPojoConverter"
        })
        void shouldReturnSimpleNameOfEntityToPojo(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.EntityToPojoConverter);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetPojoToEntityConverter",
                "Owner, OwnerPojoToEntityConverter",
                "Store, StorePojoToEntityConverter"
        })
        void shouldReturnSimpleNameOfPojoToEntity(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.PojoToEntityConverter);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRepository",
                "Owner, OwnerRepository",
                "Store, StoreRepository"
        })
        void shouldReturnSimpleNameOfRepository(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.Repository);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, Pet",
                "Owner, Owner",
                "Store, Store"
        })
        void shouldReturnSimpleNameOfResourcePojo(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.ResourcePojo);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRoutes",
                "Owner, OwnerRoutes",
                "Store, StoreRoutes"
        })
        void shouldReturnSimpleNameOfRoutes(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.Routes);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetService",
                "Owner, OwnerService",
                "Store, StoreService"
        })
        void shouldReturnSimpleNameOfServiceApi(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.ServiceApi);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetServiceProvider",
                "Owner, OwnerServiceProvider",
                "Store, StoreServiceProvider"
        })
        void shouldReturnSimpleNameOfServiceImpl(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, PrototypeClass.ServiceImpl);
            assertThat(actual).isEqualTo(expected);
        }


        @Test
        void shouldReturnSimpleNameOfBuiltinClasses() {
            assertThat(classUnderTest.toSimpleClassName(null, PrototypeClass.AlphabeticField)).isEqualTo("AlphabeticField");
            assertThat(classUnderTest.toSimpleClassName(null, PrototypeClass.AlphabeticValidator)).isEqualTo("AlphabeticValidator");
            assertThat(classUnderTest.toSimpleClassName(null, PrototypeClass.BadRequestException)).isEqualTo("BadRequestException");
            assertThat(classUnderTest.toSimpleClassName(null, PrototypeClass.DateTimeFormatConfiguration)).isEqualTo("DateTimeFormatConfiguration");

            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.UnprocessableEntityException)).isEqualTo("UnprocessableEntityException");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.BadRequestException)).isEqualTo("BadRequestException");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.GenericDataStore)).isEqualTo("GenericDataStore");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.GlobalExceptionHandler)).isEqualTo("GlobalExceptionHandler");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.ProblemConfiguration)).isEqualTo("ProblemConfiguration");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.SecureRandomSeries)).isEqualTo("SecureRandomSeries");
            assertThat(classUnderTest.toSimpleClassName("", PrototypeClass.SpringProfiles)).isEqualTo("SpringProfiles");
        }
    }

    @Nested
    class ToVariableNameTests {
        @ParameterizedTest
        @CsvSource(value = {
                "Pet, pet",
                "Owner, owner",
                "Store, store",
                "FastTrack, fastTrack"
        })
        void shouldReturnVariableName(String resource, String expected) {
            String actual = SimpleClassNameResolver.variableName(resource);
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class StaticMethodTests {

        @Test
        void shouldReturnSimpleName() {
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.Controller)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.ServiceImpl)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.ServiceApi)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.Routes)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.DataStoreApi)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.DataStoreImpl)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.Entity)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.Repository)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.EntityToPojoConverter)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.PojoToEntityConverter)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", PrototypeClass.EntityToPojoConverter)).isNotEmpty();
        }

    }

}