pluginManagement {
    // using 'gradle/plugins' instead of the traditional 'buildSrc'
    includeBuild("../../gradle/plugins")
}

dependencyResolutionManagement {
    repositories.mavenLocal()
    repositories.mavenCentral()

    // Platform for dependency versions shared by main build and build-logic
    // includeBuild("../../gradle/platform")

    versionCatalogs.create("libs") {
        from(files("../../gradle/libs.versions.toml"))
    }

}

