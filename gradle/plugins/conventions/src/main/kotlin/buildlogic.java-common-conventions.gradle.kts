plugins {
    // Apply the java Plugin to add support for Java.
    java
    id("buildlogic.jacoco-conventions")
    id("buildlogic.lint-conventions")
    id("buildlogic.sonar-jacoco-conventions")
}

repositories {
    // Allow mavenLocal to enable using snapshot versions of libraries
    // that are build+published locally before said library is published
    // to a main repository.
    mavenLocal()
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}



// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
