plugins {
    id("buildlogic.java-library-conventions")
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
    `java-test-fixtures`
}

dependencies {
    api(project(":mc-annotations"))

    implementation(libs.spring.context)
    implementation(libs.spring.core)
    
    implementation(libs.commonsBeanUtils) // this hasn't been updated since 2019; need to retire its usage
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

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)

}
// Since some projects have integrationTests, and some don't,
// the sonar.tests needs to be set for each project. Otherwise,
// it's possible for the sonar.tests value from another project
// to leak into this one, and an error will be raised that
// `The folder src/integrationTest/java' does not exist for...`
sonar {
    properties {
        property("sonar.tests", "src/test/java")
    }
}
