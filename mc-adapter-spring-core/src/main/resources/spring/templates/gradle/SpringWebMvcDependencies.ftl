dependencies {
    annotationProcessor libs.springBootConfigProcessor

    developmentOnly libs.springDevTools

<#if (project.isWithMongoDb())>
    implementation libs.springBootStarterWeb
    implementation libs.springBootStarterDataMongoDb
    implementation libs.springBootStarterValidation
    implementation libs.problemSpringWebStarter
    implementation libs.problemJacksonDataType
    implementation libs.jakartaPersistenceApi
    implementation libs.springBootStarterHateoas
<#else>
    implementation libs.springBootStarterActuator
    implementation libs.springBootStarterWeb
    implementation libs.springBootStarterDataJpa
    implementation libs.springBootStarterValidation
    implementation libs.problemSpringWeb
    implementation libs.problemJacksonDataType
    implementation libs.jakartaPersistenceApi
    implementation libs.springBootStarterHateoas
    implementation libs.rsqlJpaSpringBootStarter
</#if>
<#if project.isWithOpenApi()>
    implementation libs.openApiStarterWebMvcUI
</#if>
<#if (project.isWithLiquibase())>
    implementation libs.liquibaseCore
</#if>

    // Optional: This reports out-of-date property names
    runtimeOnly libs.springBootPropertiesMigrator

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
<#elseif (project.isWithMongoDb())>
    runtimeOnly libs.mongoDbDriverSync
<#else>
    runtimeOnly libs.h2
</#if>

    testImplementation libs.assertJ
    testImplementation (libs.springBootStarterTest)
    testImplementation (platform( libs.junitBillOfMaterial ))
    testImplementation (libs.junitJupiter)

<#if (project.isWithTestContainers())>
    testImplementation libs.springCloud
    testImplementation platform( libs.testContainersBom )
    testImplementation libs.testContainersJupiter
    testImplementation libs.springBootTestContainers
    testImplementation libs.springDevTools
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testContainersPostgres
    </#if>
    <#if (project.isWithMongoDb())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testContainersMongoDb
    </#if>
<#else>
<#-- if testcontainers aren't in use, default to using H2 to enable -->
<#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>
    testFixturesImplementation libs.jakartaPersistenceApi
}