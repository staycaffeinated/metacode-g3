dependencies {
    annotationProcessor libs.springBootConfigProcessor

    developmentOnly libs.springDevTools

    implementation libs.jacksonDatatypeJsr310
    implementation libs.r2dbcSpi
    implementation libs.springBootStarterDataR2dbc
    implementation libs.springBootStarterAop
    implementation libs.springBootStarterActuator
    implementation libs.springBootStarterWebFlux
    implementation libs.springBootStarterValidation
    implementation libs.problemSpringWebFlux
    implementation libs.problemJacksonDataType
    implementation libs.jakartaPersistenceApi
<#if project.isWithOpenApi()>
    implementation libs.openApiStarterWebfluxUI
</#if>
<#if (project.isWithLiquibase())>
    implementation libs.liquibaseCore
</#if>
<#if (project.isWithPostgres())>
    implementation libs.r2dbcPostgres
    runtimeOnly libs.postgresql
<#else>
    implementation libs.r2dbcH2
    runtimeOnly libs.h2
</#if>

    // Optional: This reports out-of-date property names
    runtimeOnly libs.springBootPropertiesMigrator

<#if (project.isWithTestContainers())>
    testImplementation libs.springCloud
    testImplementation platform( libs.testContainersBom )
    testImplementation libs.testContainersJupiter
    testImplementation libs.springBootTestContainers
    testImplementation libs.springDevTools
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testContainersPostgres
    testImplementation libs.testContainersR2DBC
    </#if>
<#else>
<#-- if testcontainers aren't in use, default to using H2 to enable -->
<#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>
    testAnnotationProcessor libs.lombok
    testCompileOnly libs.lombok
    testImplementation (libs.springBootStarterTest)
    testImplementation (platform( libs.junitBillOfMaterial ))
    testImplementation libs.junitJupiter
    testImplementation libs.reactorTest

    testFixturesImplementation libs.reactorTest
    testFixturesImplementation libs.springBootStarterDataR2dbc
    testFixturesImplementation libs.jakartaPersistenceApi
}