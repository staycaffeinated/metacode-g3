/**
* These are the plugins that need to be present in the root directory's
* build.gradle file to enable support for multimodule projects. With this
* file in place, it becomes easier to add additional modules to this project.
* For example, to add a module named, say, "core-library", the steps are:
* 1) Create a directory named "core-library"
* 2) Update settings.gradle and add the line: include "core-library"
* 3) Create the core-library/build.gradle file. That build file might look like:
* <code>
* plugins {
*       id 'buildlogic.library-conventions'
*       alias(libs.plugins.versions)
*       alias(libs.plugins.lombok.plugin)
* }
* dependencies {
*       implementation libs.guava // for example
* }
* </code>
**/

plugins {
    alias(libs.plugins.nebula.lint)
    alias(libs.plugins.sonar)
    alias(libs.plugins.lombok.plugin)
    id "buildlogic.subproject-configurations"
    id "jacoco-report-aggregation"
}

/*
 * Jacoco Report Aggregation is added so that, if subprojects
 * are added to this project, the code coverage from the subprojects
 * will be rolled up into a single code-coverage report. Remove this
 * dependencies stanza if you don't want that behavior.
 */
dependencies {
    jacocoAggregation project(":application")
}

/*
 * Define reports of type JacocoCoverageReport, which collect the coverage
 * data from unit tests and integration tests. If you add additional test facets
 * (e.g., a functionTest facet) that needs to be part of code coverage, then
 *   (1) add a corresponding coverage report entry here and
 *   (2) in the `buildlogic.subproject-configurations.gradle` plugin, and an
 *       entry to the `sonar.coverage.jacoco.xmlReportPaths', similar to the
 *       existing entries.
 */
reporting {
    reports {
        testCodeCoverageReport(JacocoCoverageReport) {
            // for gradle 13, use
            // testSuiteName = "test"
            testType = TestSuiteType.UNIT_TEST
        }
        integrationTestCodeCoverageReport(JacocoCoverageReport) {
            // for gradle 13, use
            // testSuiteName = "integrationTest"
            testType = TestSuiteType.INTEGRATION_TEST
        }
    }
}
