
plugins {
    id 'groovy-gradle-plugin'
}

dependencies {
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${project.sonarqubeVersion}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.spotlessVersion}")
    implementation("com.netflix.nebula:gradle-lint-plugin:18.0.3")
    implementation("com.google.cloud.tools:jib-gradle-plugin:${project.jibPluginVersion}")
    implementation("io.freefair.gradle:lombok-plugin:${project.lombokPluginVersion}")
    implementation(platform("org.example.platform:platform"))
    compileOnly("org.projectlombok:lombok:${project.lombokVersion}")
}