plugins {
    id("jacoco")
    id("org.sonarqube")
}

sonarqube {
    properties {
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java")
    }
}