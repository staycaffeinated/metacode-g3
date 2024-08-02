plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    id("buildlogic.integration-test")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

group = "mmm.coffee.metacode"

dependencies {
    // annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-core"))
    api(project(":mc-adapter-spring-shared"))

    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)

    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)
    testImplementation(libs.spring.boot.starter.test)
}

// Since some projects have integrationTests, and some don't,
// the sonar.tests needs to be set for each project. Otherwise,
// it's possible for the sonar.tests value from another project
// to leak into this one, and an error will be raised that
// `The folder src/integrationTest/java' does not exist for...`
sonar {
    properties {
        property("sonar.tests", "src/test/java")
    }
}
