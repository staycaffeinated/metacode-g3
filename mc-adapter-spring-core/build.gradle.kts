plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-spi"))

    implementation(libs.commonsConfig)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang3)
    implementation(libs.jmustache)
    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)
    implementation(libs.jakartaInject)

    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)

    /*
    integrationTestImplementation(platform(libs.junitBillOfMaterial))
    integrationTestImplementation(libs.junitJupiter)
    integrationTestImplementation(libs.junitSystemRules)
    integrationTestImplementation(libs.truth)
    integrationTestImplementation(libs.mockito)
    */

}
