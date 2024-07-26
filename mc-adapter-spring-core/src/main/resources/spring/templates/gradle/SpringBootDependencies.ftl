dependencies {
    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.actuator
    implementation libs.spring.boot.starter.web

    // Optional: this reports out-of-date property names.
    runtimeOnly libs.spring.boot.properties.migrator

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
}