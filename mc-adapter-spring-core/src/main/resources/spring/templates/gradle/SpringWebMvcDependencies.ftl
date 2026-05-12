dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

<#if (project.isWithMongoDb())>
    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.data.mongodb
    implementation libs.spring.boot.starter.validation
    implementation libs.jakarta.persistence.api
    implementation libs.spring.boot.starter.hateoas
<#else>
    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.data.jpa
    implementation libs.spring.boot.starter.validation
    implementation libs.jakarta.persistence.api
    implementation libs.spring.boot.starter.hateoas
    implementation libs.rsql.jpa.spring.boot.starter
</#if>
    implementation libs.jackson-core
    implementation libs.jackson-databind
<#if project.isWithOpenApi()>
    // implementation libs.openapi.starter.webmvc.ui
    implementation libs.swagger-annotations
</#if>
<#if (project.isWithLiquibase())>
    implementation libs.liquibase.core
</#if>
<#if (project.isWithFlyway())>
    implementation libs.flyway.core
</#if>
<#if (project.isWithKafka())>
    implementation libs.spring.kafka
    implementation libs.kafka.streams
    implementation libs.jackson.datatype.jsr310
</#if>

    // Optional: This reports out-of-date property names
    runtimeOnly libs.spring.boot.properties.migrator

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
    <#if (project.isWithFlyway())>
    runtimeOnly libs.flyway.database.postgresql
    </#if>
<#elseif (project.isWithMongoDb())>
    runtimeOnly libs.mongodb.driver.sync
    <#if (project.isWithFlyway())>
    runtimeOnly libs.flyway.database.nc.mongodb
    </#if>
<#else>
    runtimeOnly libs.h2
</#if>

    testImplementation libs.assertJ
    testImplementation libs.datafaker
    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
    testImplementation libs.spring.boot.starter.webmvc.test
    testImplementation libs.spring.boot.stater.jdbc.test
    testImplementation libs.spring.boot.test.autoconfigure
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
    testImplementation libs.kafka.streams.test.utils
</#if>
<#if (project.isWithFlyway())>
    testImplementation libs.flyway.spring.test
    testImplementation libs.flyway.test
</#if>

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
    <#if (project.isWithKafka())>
    testImplementation libs.testcontainers.kafka
    </#if>
<#else>
<#-- if testcontainers aren't in use, default to using H2 to enable -->
<#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>
    testFixturesImplementation libs.jakarta.persistence.api
    testFixturesImplementation libs.datafaker
}