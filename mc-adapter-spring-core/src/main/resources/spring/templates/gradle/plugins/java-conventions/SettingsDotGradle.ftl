dependencyResolutionManagement {
repositories {
mavenCentral()
gradlePluginPortal()
}
includeBuild("../platform")
}

include("java-conventions")