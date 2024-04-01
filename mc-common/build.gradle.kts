plugins {
    id("buildlogic.java-library-conventions")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

dependencies {
    api(project(":mc-annotations"))

    implementation(libs.commonsBeanUtils)
    implementation(libs.commonsConfig)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang3)
    implementation(libs.guava)
    implementation(libs.jakartaAnnotations)
    implementation(libs.jmustache)
    implementation(libs.picocli)
    implementation(libs.freemarker)
    implementation(libs.jacksonYaml)
    implementation(libs.toml)
    implementation(libs.slf4jApi)

    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)

}
