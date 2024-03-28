
plugins {
    id("buildlogic.java-application-conventions")
    id("buildlogic.versioning")
}

dependencies {
    implementation(project(":mc-annotations"))
    implementation(project(":mc-common"))

    implementation(libs.picocli.spring.boot.starter)
    implementation(libs.spring.context)
    implementation(libs.spring.core)
    implementation(libs.spring.boot.starter.logging)

    testImplementation(libs.junitJupiter)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockito)
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    // Define the main class for the application.
    mainClass = "mmm.coffee.metacode.Application"
}
