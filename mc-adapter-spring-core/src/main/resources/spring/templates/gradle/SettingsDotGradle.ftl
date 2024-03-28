pluginManagement {
    includeBuild("./gradle/settings")

}

plugins {
    id "org.example.repositories"

    // This plugin is related to:
    // https://docs.gradle.org/8.4/userguide/toolchains.html#sub:download_repositories
    id 'org.gradle.toolchains.foojay-resolver-convention' version '0.7.0'
}

rootProject.name = '${project.applicationName}'

include "application"
