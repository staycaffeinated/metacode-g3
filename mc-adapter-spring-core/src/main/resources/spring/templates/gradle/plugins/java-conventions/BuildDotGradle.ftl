plugins {
id 'groovy-gradle-plugin'
}

dependencies {
implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${project.sonarqubeVersion}")
implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.spotlessVersion}")
implementation("com.netflix.nebula:gradle-lint-plugin:19.0.2")
implementation("com.google.cloud.tools:jib-gradle-plugin:${project.jibPluginVersion}")
implementation("io.freefair.gradle:lombok-plugin:${project.lombokPluginVersion}")
implementation(platform("buildlogic.platform:platform"))
}