dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.batch
    implementation libs.spring.boot.starter.data.jpa
<#if (project.isWithKafka())>
    implementation libs.spring.kafka
</#if>

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
</#if>
    // Optional: This reports out-of-date property names
    runtimeOnly libs.spring.boot.properties.migrator

<#if (project.isWithTestContainers())>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.testcontainers.jupiter
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testcontainers.postgres
    </#if>
</#if>

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
    testImplementation libs.spring.batch.test
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
</#if>
}