plugins {
    id "java-library"
    id "jvm-test-suite"
}

// --------------------------------------------------------------------------------
// Make all tests use JUnit 5
// --------------------------------------------------------------------------------
tasks.withType(Test).configureEach {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
}

