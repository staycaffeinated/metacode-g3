plugins {
    id "java-library"
    id "java-test-fixtures"
    id "org.example.testing"
    id "org.example.sonar-jacoco"
    id "org.example.jacoco"
    id "org.example.spotless"
    id "org.example.lint"
    id "org.example.docker"
}

// --------------------------------------------------------------------------------
// Enable compiling with a specific Java version,
// independent of the developer's current Java version.
// The java version can be pinned by setting the version in the ".java-version"
// file found in the root project. Fall back to version 17 if anything goes wrong.
// --------------------------------------------------------------------------------
java {
    def javaVersion = 17
    try {
        javaVersion = rootProject.file('.java-version').text.trim()
    }
    catch (Exception e) {
        javaVersion = 17
    }
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

/**
 * Example of setting lint arguments
 */
tasks.withType(JavaCompile) {
    options.compilerArgs += ["-Xlint:unchecked"]
}

// Share sources folder with other projects for aggregated JavaDoc and JaCoCo reports
configurations.create("transitiveSourcesElements") {
    visible = false
    canBeResolved = false
    canBeConsumed = true
    extendsFrom(configurations.implementation)
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage, Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category, Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType, "source-folders"))
    }
    sourceSets.main.java.srcDirs.forEach { outgoing.artifact(it) }
}
