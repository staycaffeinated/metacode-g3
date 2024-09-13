subprojects {
    sonarqube {
        properties {
            property "sonar.sources", "src/main"
            property "sonar.tests", [ "src/test", "src/integrationTest" ]
            property "sonar.coverage.jacoco.xmlReportPaths", [
<#noparse>
                "${project.buildDir}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml",
                "${project.buildDir}/reports/jacoco/integrationTestCodeCoverageReport/integrationTestCodeCoverageReport.xml"    ]
</#noparse>
        }
    }
    task("sonar").dependsOn("jacocoTestReport")
}
