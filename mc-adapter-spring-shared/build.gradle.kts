plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
    `java-test-fixtures`
}


dependencies {
    implementation(project(":mc-common"))
    implementation(project(":mc-annotations"))
    implementation(libs.freemarker)
    implementation(libs.guice)
    implementation(libs.jacksonYaml)

    testImplementation(libs.spring.boot.starter.test)

    testFixturesImplementation(project(":mc-common"))
    testFixturesImplementation(project(":mc-adapter-spring-core"))  // to enable visibility to catalogs and templates
    testFixturesImplementation(project(":mc-adapter-spring-webmvc")) // to enable visibility to catalogs and templates
    testFixturesImplementation(libs.freemarker)
    testFixturesImplementation(libs.guice)
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