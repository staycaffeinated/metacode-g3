plugins {
    id("java-library")
    id("jvm-test-suite")
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

// Wire integrationTest exec data into jacocoTestReport when jacoco is present
plugins.withId("jacoco") {
    tasks.named("jacocoTestReport") {
        dependsOn(tasks.named("integrationTest"))
    }
}
