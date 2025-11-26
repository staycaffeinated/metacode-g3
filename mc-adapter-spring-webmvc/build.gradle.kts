plugins {
    id("jvm-test-suite")
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    id("buildlogic.integration-test")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-core"))
    api(project(":mc-adapter-spring-shared"))

    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.junit.platform.runner)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)
    testImplementation(libs.spring.boot.starter.test)
}

sonar {
    properties {
        property("sonar.tests", "src/test/java,src/integrationTest/java")
    }
}
