plugins {
    `kotlin-dsl`
}

group = "buildlogic"

dependencies {
    // implementation(platform("mmm.coffee.metacode:platform"))
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:5.0.0.4638")
    implementation("se.solrike.sonarlint:sonarlint-gradle-plugin:2.0.0")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.25.0")
    implementation("com.palantir.javaformat:gradle-palantir-java-format:2.43.0")
    implementation("io.freefair.gradle:lombok-plugin:8.6")
    // compileOnly("org.projectlombok:lombok:1.18.28")
}


