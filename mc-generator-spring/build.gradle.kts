plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.integration-test")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-shared"))
    api(project(":mc-adapter-spring-core"))
    api(project(":mc-adapter-spring-webmvc"))

    implementation(libs.spring.context)
    implementation(libs.commonsConfig)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang3)
    implementation(libs.jmustache)
    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)
    implementation(libs.jakartaInject)

    // test 
    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)
    testRuntimeOnly(libs.junit.platform.runner)
}

/**
// https://blog.gradle.org/introducing-test-suites
val integrationTest = sourceSets.create("integrationTest") {}

configurations["integrationTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    useJUnitPlatform()

    classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + integrationTest.output
    testClassesDirs = integrationTest.output.classesDirs
}

dependencies {
    // configurations["integrationTestImplementation"](project)
}
**/


