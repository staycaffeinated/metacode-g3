plugins {
    id 'groovy-gradle-plugin'
}

dependencies {
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${project.versions["sonarqube"]}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.versions["spotless"]}")
    implementation("se.solrike.sonarlint:sonarlint-gradle-plugin:${project.versions["sonarlint"]}")
    implementation("com.google.cloud.tools:jib-gradle-plugin:${project.versions["jibPlugin"]}")
}