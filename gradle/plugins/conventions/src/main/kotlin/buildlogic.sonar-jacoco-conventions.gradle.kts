plugins {
    id("jacoco")
    id("org.sonarqube")
}

sonar {
    properties {
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java, src/integrationTest/java")
    }
}
