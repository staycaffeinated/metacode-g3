pluginManagement {
    // Tells Gradle we're using 'gradle/plugins' instead of the traditional 'buildSrc'
    includeBuild("./gradle/plugins")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
