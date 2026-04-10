dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web
<#if (project.isWithKafka())>
    implementation libs.spring.kafka
    implementation libs.kafka.streams
    implementation libs.jackson.datatype.jsr310
</#if>

    // Optional: this reports out-of-date property names.
    runtimeOnly libs.spring.boot.properties.migrator

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
    testImplementation libs.kafka.streams.test.utils
    <#if project.isWithTestContainers()>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.testcontainers.jupiter
    testImplementation libs.testcontainers.kafka
    </#if>
</#if>
}