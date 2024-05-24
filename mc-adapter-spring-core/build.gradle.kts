plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-shared"))

    implementation(libs.spring.core)
    implementation(libs.spring.context)
    implementation(libs.commonsConfig)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang3)
    implementation(libs.jmustache)
    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)
    implementation(libs.jakartaInject)

    // testImplementation(platform(libs.junitBillOfMaterial)) Spring manages this
    testImplementation(libs.spring.boot.starter.test)
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
