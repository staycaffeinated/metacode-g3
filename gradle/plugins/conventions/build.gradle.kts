plugins {
    `kotlin-dsl`
}

group = "buildlogic"

dependencies {
    // implementation(platform("mmm.coffee.metacode:platform"))
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.1.0.3113")
    implementation("se.solrike.sonarlint:sonarlint-gradle-plugin:1.0.0-beta.15")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.19.0")
    implementation("com.palantir.javaformat:gradle-palantir-java-format:2.32.0")
    implementation("io.freefair.gradle:lombok-plugin:8.0.1")
    compileOnly("org.projectlombok:lombok:1.18.28")
}


