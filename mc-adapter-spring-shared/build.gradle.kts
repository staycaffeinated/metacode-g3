plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}


dependencies {
    implementation(project(":mc-common"))
    implementation(project(":mc-annotations"))
    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
}