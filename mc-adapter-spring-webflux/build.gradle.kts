plugins {
    id("buildlogic.java-library-conventions")
    id("buildlogic.versioning")
    id("buildlogic.integration-test")
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
}

group = "mmm.coffee.metacode"

dependencies {
    // annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
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
    testImplementation(libs.spring.boot.starter.test)
}

//tasks.jacocoTestReport {
//    reports {
//        xml.required.set(true)
//    }
//}

//sonar {
//    properties {
//        property("sonar.projectName", "mc-adapter-spring-webflux")
//        property("sonar.projectKey", "mmm.coffee.metacode:mc-adapter-spring-webflux")
//    }
//}
