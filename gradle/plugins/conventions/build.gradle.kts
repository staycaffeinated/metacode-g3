plugins {
    `kotlin-dsl`
}

group = "buildlogic"

dependencies {
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:6.3.1.5724")
    implementation("se.solrike.sonarlint:sonarlint-gradle-plugin:2.2.0")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.0.0")
    implementation("com.palantir.javaformat:gradle-palantir-java-format:2.83.0")
    implementation("io.freefair.gradle:lombok-plugin:9.1.0")
}


