pluginManagement {
// Tells Gradle we're using 'gradle/plugins' instead of the traditional 'buildSrc'
includeBuild("./gradle/plugins")
}

dependencyResolutionManagement {
repositories {
mavenCentral()
}

// Platform for dependency versions shared by main build and build-logic
includeBuild("./gradle/platform")
}
