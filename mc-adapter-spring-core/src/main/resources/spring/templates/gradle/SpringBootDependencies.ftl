dependencies {
    // enable Mockito Java Agents
    <#noparse>mockitoAgent("org.mockito:mockito-core") { transitive = false }</#noparse>

    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web
<#if (project.isWithKafka())>
    implementation libs.spring.boot.starter.kafka
    implementation libs.apache.kafka.streams
    implementation libs.jackson.datatype.jsr310
</#if>

    // Optional: this reports out-of-date property names.
    runtimeOnly libs.spring.boot.properties.migrator

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
    testImplementation libs.apache.kafka.streams.test.utils
    <#if project.isWithTestContainers()>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.testcontainers.jupiter
    testImplementation libs.testcontainers.kafka
    </#if>
</#if>

}