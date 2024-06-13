/*
 * Copyright 2022-2024 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({
        "java:S115" // enum values do not need to be in all-cap's
})
public enum Archetype {
    // project-scope archetypes
    // configs
    ApplicationConfiguration("ApplicationConfiguration"),
    DateTimeFormatConfiguration("DateTimeFormatConfiguration"),
    ProblemConfiguration("ProblemConfiguration"),
    LocalDateConverter("LocalDateConverter"),
    WebMvcConfiguration("WebMvcConfiguration"),
    SpringProfiles("SpringProfiles"),

    // converters
    EntityToPojoConverter("EjbToPojoConverter"),
    PojoToEntityConverter("PojoToEjbConverter"),

    // exceptions and error handling
    GlobalExceptionHandler("GlobalExceptionHandler"),
    BadRequestException("BadRequestException"),
    ResourceNotFoundException("ResourceNotFoundException"),
    UnprocessableEntityException("UnprocessableEntityException"),
    Exception("Exception"), // can this replace specific exceptions?

    // validation and annotations
    ResourceIdTrait("ResourceIdTrait"),
    AlphabeticAnnotation("AlphabeticAnnotation"), 
    AlphabeticValidator("AlphabeticValidator"),
    ResourceIdAnnotation("ResourceIdAnnotation"), // todo: sb: ResourceIdFieldAnnotation
    ResourceIdValidator("ResourceIdValidator"),
    SecureRandomSeries("SecureRandomSeries"), // todo: sb ResourceIdGenerator
    ResourceIdSupplier("ResourceIdSupplier"), // interface for ResourceIdGenerator
    OnUpdateAnnotation("OnUpdateAnnotation"),
    OnCreateAnnotation("OnCreateAnnotation"),
    SearchTextAnnotation("SearchTextAnnotation"),
    SearchTextValidator("SearchTextValidator"),


    // filters
    SecurityResponseHeadersFilter("SecurityResponseHeadersFilter"),

    // index/root controller & service
    DefaultController("DefaultController"), // handles the basePath, such as "/petstore/"
    DefaultService("DefaultService"),
    RootController("RootController"),
    RootService("RootService"),


    // persistence

    CustomRepository("CustomRepository"),
    GenericDataStore("GenericDataStore"),
    DatabaseTablePopulator("DatabaseTablePopulator"),   // for demo mode; populates sample table
    RegisterDatabaseProperties("RegisterDatabaseProperties"), // db config for integration tests
    ContainerConfiguration("ContainerConfiguration"), // testcontainer config for integration tests
    MongoContainerConfiguration("MongoContainerConfiguration"),
    MongoDatabaseConfiguration("MongoDatabaseConfiguration"),
    MongoRegisterDatabaseProperties("MongoRegisterDatabaseProperties"),
    MongoDbContainerTests("MongoDbContainerTests"),
    PostgresDbContainerTests("PostgresDbContainerTests"),
    DatabaseInitFunction("DatabaseInitFunction"),

    // miscellaneous
    Application("Application"), // the main class
    AbstractIntegrationTest("AbstractIntegrationTest"),
    AbstractDataJpaTest("AbstractDataJpaTest"),
    ConversionService("ConversionService"), // todo: I think this is endpoint dependent

    /*
     * Endpoint-scope archetypes
     */

    ServiceApi("ServiceApi"),
    ServiceImpl("ServiceImpl"),
    Controller("Controller"),
    Routes("Routes"),
    ResourcePojo("Pojo"),
    DataStoreApi("DataStoreApi"),
    DataStoreImpl("DataStoreImpl"),
    MongoDataStore("MongoDataStore"),
    Entity("Entity"),
    EntityResource("EntityResource"), // the POJO view of an EJB
    Repository("Repository"),
    PersistenceAdapter("PersistenceAdapter"),
    ObjectDataStore("ObjectDataStore"),
    ObjectDataStoreProvider("ObjectDataStoreProvider"), // persistence adapter for a specific EJB/POJO
    CustomSQLRepository("CustomSQLRepository"),
    ExceptionHandler("ExceptionHandler"), // handles exceptions from a specific endpoint
    EntityWithText("EntityWithText"), // a predicate for a given EJB/POJO

    // for plain text files, like the README.adoc, lombok.config, settings.gradle, etc.
    Text("Text"),

    // Anything that's not mapped
    Undefined("Undefined");

    private static final Map<String, Archetype> PROTOTYPE_MAP =
            Stream.of(Archetype.values()).collect(Collectors.toMap(s -> s.stringValue, Function.identity()));

    private final String stringValue;

    Archetype(String value) {
        this.stringValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Enables Jackson library to handle undefined values gracefully.
     * Unknown values will be mapped to the `Unknown` value.
     * @param value the text being mapped to a `PrototypeClass` value
     * @return the `PrototypeClass` equivalent of `value`
     */
    @JsonCreator
    public static Archetype fromString(String value) {
        return Optional.ofNullable(PROTOTYPE_MAP.get(value)).orElse(Archetype.Undefined);
    }
}