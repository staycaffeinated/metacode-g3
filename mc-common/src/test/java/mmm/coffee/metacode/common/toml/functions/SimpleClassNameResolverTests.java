package mmm.coffee.metacode.common.toml.functions;

import mmm.coffee.metacode.common.model.Archetype;
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
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.Controller);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRepository",
                "Owner, OwnerRepository",
                "Store, StoreRepository"
        })
        void shouldReturnSimpleNameOfCustomRepository(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.CustomRepository);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetDataStore",
                "Owner, OwnerDataStore",
                "Store, StoreDataStore"
        })
        void shouldReturnSimpleNameOfDataStoreApi(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.DataStoreApi);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetDataStoreProvider",
                "Owner, OwnerDataStoreProvider",
                "Store, StoreDataStoreProvider"
        })
        void shouldReturnSimpleNameOfDataStoreImpl(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.DataStoreImpl);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetEntity",
                "Owner, OwnerEntity",
                "Store, StoreEntity"
        })
        void shouldReturnSimpleNameOfEntity(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.Entity);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetEntityToPojoConverter",
                "Owner, OwnerEntityToPojoConverter",
                "Store, StoreEntityToPojoConverter"
        })
        void shouldReturnSimpleNameOfEntityToPojo(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.EntityToPojoConverter);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetPojoToEntityConverter",
                "Owner, OwnerPojoToEntityConverter",
                "Store, StorePojoToEntityConverter"
        })
        void shouldReturnSimpleNameOfPojoToEntity(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.PojoToEntityConverter);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRepository",
                "Owner, OwnerRepository",
                "Store, StoreRepository"
        })
        void shouldReturnSimpleNameOfRepository(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.Repository);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, Pet",
                "Owner, Owner",
                "Store, Store"
        })
        void shouldReturnSimpleNameOfResourcePojo(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.ResourcePojo);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetRoutes",
                "Owner, OwnerRoutes",
                "Store, StoreRoutes"
        })
        void shouldReturnSimpleNameOfRoutes(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.Routes);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetService",
                "Owner, OwnerService",
                "Store, StoreService"
        })
        void shouldReturnSimpleNameOfServiceApi(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.ServiceApi);
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Pet, PetServiceProvider",
                "Owner, OwnerServiceProvider",
                "Store, StoreServiceProvider"
        })
        void shouldReturnSimpleNameOfServiceImpl(String resource, String expected) {
            String actual = classUnderTest.toSimpleClassName(resource, Archetype.ServiceImpl);
            assertThat(actual).isEqualTo(expected);
        }


        @Test
        void shouldReturnSimpleNameOfBuiltinClasses() {
            assertThat(classUnderTest.toSimpleClassName(null, Archetype.AlphabeticAnnotation)).isEqualTo("AlphabeticAnnotation");
            assertThat(classUnderTest.toSimpleClassName(null, Archetype.AlphabeticValidator)).isEqualTo("AlphabeticValidator");
            assertThat(classUnderTest.toSimpleClassName(null, Archetype.BadRequestException)).isEqualTo("BadRequestException");
            assertThat(classUnderTest.toSimpleClassName(null, Archetype.DateTimeFormatConfiguration)).isEqualTo("DateTimeFormatConfiguration");

            assertThat(classUnderTest.toSimpleClassName("", Archetype.UnprocessableEntityException)).isEqualTo("UnprocessableEntityException");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.BadRequestException)).isEqualTo("BadRequestException");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.GenericDataStore)).isEqualTo("GenericDataStore");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.GlobalExceptionHandler)).isEqualTo("GlobalExceptionHandler");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.ProblemConfiguration)).isEqualTo("ProblemConfiguration");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.SecureRandomSeries)).isEqualTo("SecureRandomSeries");
            assertThat(classUnderTest.toSimpleClassName("", Archetype.SpringProfiles)).isEqualTo("SpringProfiles");
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
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.Controller)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.ServiceImpl)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.ServiceApi)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.Routes)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.DataStoreApi)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.DataStoreImpl)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.Entity)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.Repository)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.EntityToPojoConverter)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.PojoToEntityConverter)).isNotEmpty();
            assertThat(SimpleClassNameResolver.simpleClassName("Book", Archetype.EntityToPojoConverter)).isNotEmpty();
        }

    }

}