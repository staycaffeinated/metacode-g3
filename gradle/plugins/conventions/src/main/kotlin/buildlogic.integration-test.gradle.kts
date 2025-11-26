plugins {
    id("java-library")
    id("jvm-test-suite")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging { events("passed", "skipped", "failed") }
}

testing {
    suites {
        // Define a new test suite named 'integrationTest'
        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter()

            dependencies {
                // Add all dependencies from the `test` suite
                implementation(project.configurations["testRuntimeClasspath"])
                runtimeOnly(project.configurations["testRuntimeClasspath"])

                // Ensure the main project output is available
                implementation(project())
            }
        }
    }
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