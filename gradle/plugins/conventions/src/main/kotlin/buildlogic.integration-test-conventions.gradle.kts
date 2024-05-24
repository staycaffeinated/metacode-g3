/*
 * This plugin adds the facet 'integrationTest' to a module.
 *
 * THIS IS BROKEN. NOT SURE WHY GRADLE BALKS AT IT
 */
plugins {
    id("java-library")
    id("jvm-test-suite")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed", "skipped", "failed")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("integrationTest") {
            testType.set(TestSuiteType.INTEGRATION_TEST)
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}
tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}
