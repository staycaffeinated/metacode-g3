
subprojects {
    sonarqube {
        properties {
            property "sonar.sources", "src/main"
            property "sonar.tests", [ "src/test", "src/integrationTest" ]
            property "sonar.coverage.jacoco.xmlReportPaths", [
<#noparse>
                    "${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml",
                    "${project.buildDir}/reports/jacoco/integrationTest/jacocoTestReport.xml" ]
</#noparse>
        }
    }

    task("sonar").dependsOn("jacocoTestReport")
}
