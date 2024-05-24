plugins {
    id("java-library")
    id("jvm-test-suite")
}

// https://blog.gradle.org/introducing-test-suites
val integrationTest = sourceSets.create("integrationTest") {}

configurations["integrationTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    useJUnitPlatform()

    // Including sourceSets.main.output enables integration tests to have visibility to `src/main/resources`
    classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + integrationTest.output + sourceSets.main.get().output
    testClassesDirs = integrationTest.output.classesDirs
}
tasks.named("integrationTest") {
    shouldRunAfter("test")
}
tasks.named("check") {
    dependsOn("integrationTest")
}

/**
 * Maybe also needs this added in dependencies:
 *
 * dependencies {
 *  configurations["integrationTestImplementation"](project)
 * }
 *
 */