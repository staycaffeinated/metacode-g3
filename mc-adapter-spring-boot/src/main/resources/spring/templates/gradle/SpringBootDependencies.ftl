dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web
<#if (project.isWithKafka())>
    implementation libs.spring.kafka
</#if>

    // Optional: this reports out-of-date property names.
    runtimeOnly libs.spring.boot.properties.migrator

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
</#if>
}