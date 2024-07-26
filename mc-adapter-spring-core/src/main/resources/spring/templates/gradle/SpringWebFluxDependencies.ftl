dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.jackson.datatype.jsr310
    implementation libs.r2dbc.spi
    implementation libs.spring.boot.starter.data.r2dbc
    implementation libs.spring.boot.starter.aop
    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.webflux
    implementation libs.spring.boot.starter.validation
    implementation libs.problem.spring.webflux
    implementation libs.problem.jackson.datatype
    implementation libs.jakarta.persistence.api
<#if project.isWithOpenApi()>
    implementation libs.openapi.starter.webflux.ui
</#if>
<#if (project.isWithLiquibase())>
    implementation libs.liquibase.core
</#if>
<#if (project.isWithPostgres())>
    implementation libs.r2dbc.postgres
    runtimeOnly libs.postgresql
<#else>
    implementation libs.r2dbc.h2
    runtimeOnly libs.h2
</#if>

    // Optional: This reports out-of-date property names
    runtimeOnly libs.spring.boot.properties.migrator

<#if (project.isWithTestContainers())>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.testcontainers.jupiter
    testImplementation libs.spring.devtools
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testcontainers.postgres
    testImplementation libs.testcontainers.r2dbc
    </#if>
<#else>
<#-- if testcontainers aren't in use, default to using H2 to enable -->
<#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>
    testAnnotationProcessor libs.lombok
    testCompileOnly libs.lombok
    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
    testImplementation libs.reactor.test

    testFixturesImplementation libs.reactor.test
    testFixturesImplementation libs.spring.boot.starter.data.r2dbc
    testFixturesImplementation libs.jakarta.persistence.api
}