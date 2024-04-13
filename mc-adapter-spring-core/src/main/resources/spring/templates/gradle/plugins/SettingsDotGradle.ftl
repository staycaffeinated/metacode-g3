dependencyResolutionManagement {
repositories.mavenCentral()
repositories.gradlePluginPortal()
includeBuild("../platform")

versionCatalogs {
libs {
from(files("../libs.versions.toml"))
}
}
}

include("java-conventions")