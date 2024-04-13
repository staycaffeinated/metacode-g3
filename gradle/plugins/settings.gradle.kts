dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
    includeBuild("../platform")

    versionCatalogs.create("libs") {
        from(files("../libs.versions.toml"))
    }
}

include("conventions")
include("versioning")
