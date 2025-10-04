plugins {
    id "java-library"
    id "jvm-test-suite"
}

// --------------------------------------------------------------------------------
// Make all tests use JUnit 5
// --------------------------------------------------------------------------------
tasks.withType(Test) {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
}

// --------------------------------------------------------------------------------
// Define configurations for integrationTest and performanceTest
// --------------------------------------------------------------------------------
testing {
    suites {
        test {
            useJUnitJupiter()
        }
        /*
         * Define the integration test suite
         */
        integrationTest(JvmTestSuite) {
            // This is not supported in Gradle 8.13 and newer, but is needed in older versions
            // testType.set(TestSuiteType.INTEGRATION_TEST)
            dependencies {
                implementation project()
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
            configurations {
                integrationTestImplementation {
                    extendsFrom testImplementation
                }
            }
        }

        /*
         * Define the performance test suite
         */
        performanceTest(JvmTestSuite) {
            // This is not supported in Gradle 8.13 and newer, but is needed in older versions
            // testType.set(TestSuiteType.PERFORMANCE_TEST)
            dependencies {
                implementation project()
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }

            configurations {
                performanceTestImplementation {
                    extendsFrom testImplementation
                }
            }
        }
    }
}

/*
 * The 'check' task will trigger integration tests.
 */
tasks.named('check') {
    dependsOn(testing.suites.integrationTest)
}
