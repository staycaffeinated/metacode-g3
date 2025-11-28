plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
    `java-test-fixtures`
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
    implementation(libs.jacksonYaml)
    implementation(libs.slf4jApi)
    implementation(libs.jakartaInject)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junitSystemRules)
    testImplementation(libs.truth)
    testImplementation(libs.mockito)

    testFixturesImplementation(libs.mockito)
    testFixturesImplementation(libs.commonsConfig)
    testFixturesImplementation(libs.spring.core)
    testFixturesImplementation(libs.freemarker)
    testFixturesImplementation(project(":mc-common"))
    testFixturesImplementation(project(":mc-adapter-spring-shared"))

}
// Since some projects have integrationTests, and some don't,
// the sonar.tests needs to be set for each project. Otherwise,
// it's possible for the sonar.tests value from another project
// to leak into this one, and an error will be raised that
// `The folder src/integrationTest/java' does not exist for...`
sonar {
    properties {
        property("sonar.tests", "src/test/java")

        property("sonar.coverage.exclusions",
            "**/WebMvcTemplateModel.java"  // this is a pojo decored with lombok:Data
            + ",**/EdgeCaseResolvedArchetypeDescriptor**"
        )
    }
}
