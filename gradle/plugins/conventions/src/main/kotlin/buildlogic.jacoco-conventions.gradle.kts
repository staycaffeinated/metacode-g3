plugins {
    id("java-library")
    id("jacoco")
}


tasks.jacocoTestReport {
    enabled = true

    // Pick up exec files from all test tasks (test + integrationTest when present)
    executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/*.exec"))

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// Share the coverage data to be aggregated for the whole product

//configurations.create("coverageDataElements") {
//    isVisible = false
//    isCanBeResolved = false
//    isCanBeConsumed = true
//    extendsFrom(configurations.implementation.get())
//    attributes {
//        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
//        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
//        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("jacoco-coverage-data"))
//    }
//    outgoing.artifact(tasks.test.map { task ->
//        task.extensions.getByType<JacocoTaskExtension>().destinationFile!!
//    })
//}


