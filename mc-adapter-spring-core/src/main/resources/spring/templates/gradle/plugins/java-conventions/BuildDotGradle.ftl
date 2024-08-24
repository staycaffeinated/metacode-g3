plugins {
    id 'groovy-gradle-plugin'
}

dependencies {
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${project.versions["sonarqube"]}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.versions["spotless"]}")
    implementation("com.netflix.nebula:gradle-lint-plugin:19.0.2")
    implementation("com.google.cloud.tools:jib-gradle-plugin:${project.versions["jibPlugin"]}")
    implementation("io.freefair.gradle:lombok-plugin:${project.versions["lombokPlugin"]}")
    implementation(platform("buildlogic.platform:platform"))
}