plugins {
    id "java-library"
    id "jvm-test-suite"
}

configurations {
    mockitoAgent
}

// --------------------------------------------------------------------------------
// Make all tests use JUnit 5
// --------------------------------------------------------------------------------
tasks.withType(Test).configureEach {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
    jvmArgs "--add-opens", "java.base/java.lang=ALL-UNNAMED"
    doFirst {
        <#noparse>jvmArgs  "-javaagent:${configurations.mockitoAgent.asPath}"</#noparse>
    }
}

