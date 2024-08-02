import gradle.kotlin.dsl.accessors._6c9d2fa5bbab342f1acb2c142665b5dd.implementation

plugins {
    id("jacoco")
    id("org.sonarqube")
}

sonar {
    properties {
        property("sonar.sources", "src/main/java")
        property("sonar.tests", "src/test/java, src/integrationTest/java")
    }
}

/*
 * Enabling this block yields this error:
 * Cannot add a configuration with name 'coverageDataElements' as a configuration with that name already exists.
 * 
configurations.create("coverageDataElements") {
    isVisible = false
    isCanBeResolved = false
    isCanBeConsumed = true
    extendsFrom(configurations.implementation.get())
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("jacoco-coverage-data"))
    }
    tasks.withType<Test>().map { task -> task.extensions.getByType<JacocoTaskExtension>().destinationFile!! }
    // Groovy syntax:
    //    outgoing.artifact(tasks.test.map { task ->
    //        task.extensions.getByType<JacocoTaskExtension>().destinationFile!!
    //    })
}
*/