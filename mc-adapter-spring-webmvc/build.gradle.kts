plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-core"))
    api(project(":mc-adapter-spring-shared"))

    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)

    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)

}
