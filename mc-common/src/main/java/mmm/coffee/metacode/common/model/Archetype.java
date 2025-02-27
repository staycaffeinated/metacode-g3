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
    WebFluxConfiguration("WebFluxConfiguration"),
    SpringProfiles("SpringProfiles"),
    BatchConfiguration("BatchConfiguration"),
    JobCompletionNotificationListener("JobCompletionNotificationListener"),
    KafkaProducerConfiguration("KafkaProducerConfiguration"),
    KafkaConsumerConfiguration("KafkaConsumerConfiguration"),
    KafkaTopicsConfiguration("KafkaTopicsConfiguration"),

    // converters
    EntityToPojoConverter("EntityToPojoConverter"),
    PojoToEntityConverter("PojoToEntityConverter"),
    PojoToDocumentConverter("PojoToDocumentConverter"),
    DocumentToPojoConverter("DocumentToPojoConverter"),

    // exceptions and error handling
    GlobalExceptionHandler("GlobalExceptionHandler"),
    GlobalErrorWebExceptionHandler("GlobalErrorWebExceptionHandler"),
    GlobalErrorAttributes("GlobalErrorAttributes"),
    BadRequestException("BadRequestException"),
    ResourceNotFoundException("ResourceNotFoundException"),
    UnprocessableEntityException("UnprocessableEntityException"),
    Exception("Exception"), // can this replace specific exceptions?
    ProblemSummary("ProblemSummary"), // this entity is retired

    // validation and annotations
    ResourceIdTrait("ResourceIdTrait"),
    UpdateAwareConverter("UpdateAwareConverter"),
    AlphabeticAnnotation("AlphabeticAnnotation"), 
    AlphabeticValidator("AlphabeticValidator"),
    ResourceIdAnnotation("ResourceIdAnnotation"),
    ResourceIdValidator("ResourceIdValidator"),
    SecureRandomSeries("SecureRandomSeries"),
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
    RootControllerExceptionHandler("RootControllerExceptionHandler"),
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
    AbstractPostgresIntegrationTest("AbstractPostgresIntegrationTest"),

    /*
     * Endpoint-scope archetypes
     */

    ServiceApi("ServiceApi"),
    ServiceImpl("ServiceImpl"),
    Controller("Controller"),
    ControllerExceptionHandler("ControllerExceptionHandler"),
    ConversionService("ConversionService"),
    Document("Document"), // nosql Document objects
    Routes("Routes"),
    Pojo("Pojo"),
    AbstractDataStoreApi("AbstractDataStoreApi"),
    AbstractDataStoreImpl("AbstractDataStoreImpl"),
    MongoDataStoreApi("MongoDataStoreApi"),
    MongoDataStoreImpl("MongoDataStoreImpl"),
    MongoCrudAwareApi("MongoCrudAwareApi"),
    Entity("Entity"),   // the EJB view of a POJO
    EntityResource("EntityResource"), // the POJO view of an EJB
    Repository("Repository"),
    PersistenceAdapter("PersistenceAdapter"),
    ConcreteDataStoreApi("ConcreteDataStoreApi"), // api for the kind of pojo persisted as ejb's (eg: DataStore<Book>)
    ConcreteDocumentStoreApi("ConcreteDocumentStoreApi"), // api for the kind of pojo persisted as documents (eg: DocumentStore<Book>)
    ConcreteDataStoreImpl("ConcreteDataStoreImpl"), // impl of the data store, eg `BookDataStore extends DataStore<Book>`
    ConcreteDocumentStoreImpl("ConcreteDocumentStoreImpl"), // impl of the document store, eg: BookDocumentStore extends DocumentStore<Book>
    CustomSQLRepository("CustomSQLRepository"),
    // WebMvcEjbTestFixtures("WebMvcEjbTestFixtures"),
    // WebMvcModelTestFixtures("WebMvcModelTestFixtures"),
    DocumentTestFixtures("DocumentTestFixtures"),
    PojoTestFixtures("PojoTestFixtures"), // used when using MongoDB to represent POJOs

    // webflux stuff
    TestTableInitializer("TestTableInitializer"), // to init a table during testing. NB: use test-fixtures to init tables instead.
    TableInitializer("TableInitializer"),
    EjbTestFixtures("EjbTestFixtures"), // mirror of WebMvcEjbTestFixtures
    ModelTestFixtures("ModelTestFixtures"),
    ResourceIdentity("ResourceIdentity"), // makes ResourceId a type
    EndpointConfiguration("EndpointConfiguration"), // registers a converter for each kind of pojo/ejb
    EntityEvent("EntityEvent"), // an event object
    EntityEventPublisher("EntityEventPublisher"),
    TestDatabaseConfiguration("TestDatabaseConfiguration"),
    EntityCallback("EntityCallback"),

    EntityWithText("EntityWithText"), // a predicate for a given EJB/POJO
    EntitySpecification("EntitySpecification"), // an improved predicate class

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