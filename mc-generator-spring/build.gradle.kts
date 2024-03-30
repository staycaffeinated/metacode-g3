plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
}

dependencies {
    api(project(":mc-common"))
    api(project(":mc-annotations"))
    api(project(":mc-adapter-spring-shared"))
    api(project(":mc-adapter-spring-core"))
    api(project(":mc-adapter-spring-webmvc"))

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

    // test 
    testImplementation(platform(libs.junitBillOfMaterial))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)
}



