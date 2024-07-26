dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

<#if (project.isWithMongoDb())>
    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.data.mongodb
    implementation libs.spring.boot.starter.validation
    implementation libs.problem.spring.web.starter
    implementation libs.problem.jackson.datatype
    implementation libs.jakarta.persistence.api
    implementation libs.spring.boot.starter.hateoas
<#else>
    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.data.jpa
    implementation libs.spring.boot.starter.validation
    implementation libs.problem.spring.web.starter
    implementation libs.problem.jackson.datatype
    implementation libs.jakarta.persistence.api
    implementation libs.spring.boot.starter.hateoas
    implementation libs.rsql.jpa.spring.boot.starter
</#if>
<#if project.isWithOpenApi()>
    implementation libs.openapi.starter.webmvc.ui
</#if>
<#if (project.isWithLiquibase())>
    implementation libs.liquibase.core
</#if>

    // Optional: This reports out-of-date property names
    runtimeOnly libs.spring.boot.properties.migrator

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
<#elseif (project.isWithMongoDb())>
    runtimeOnly libs.mongodb.driver.sync
<#else>
    runtimeOnly libs.h2
</#if>

    testImplementation libs.assertJ
    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter

<#if (project.isWithTestContainers())>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.spring.devtools
    testImplementation libs.testcontainers.jupiter
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testcontainers.postgres
    </#if>
    <#if (project.isWithMongoDb())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testcontainers.mongodb
    </#if>
<#else>
<#-- if testcontainers aren't in use, default to using H2 to enable -->
<#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>
    testFixturesImplementation libs.jakarta.persistence.api
}