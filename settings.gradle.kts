
pluginManagement {
    // Include 'plugins build' to define convention plugins.
    // includeBuild("build-logic")
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "metacode-g3"
include("mc-application") 
include("mc-annotations")
include("mc-common")

include("mc-adapter-spring-batch")
include("mc-adapter-spring-boot")
include("mc-adapter-spring-core")
include("mc-adapter-spring-shared")
include("mc-adapter-spring-webmvc")
include("mc-adapter-spring-webflux")

include("mc-generator-spring")

dependencyResolutionManagement {
  includeBuild("gradle/plugins")
}  

