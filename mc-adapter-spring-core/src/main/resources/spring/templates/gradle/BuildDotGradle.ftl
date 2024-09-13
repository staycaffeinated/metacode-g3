plugins {
    id 'buildlogic.application-conventions'
    id 'jacoco-report-aggregation'
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.versions)
    alias(libs.plugins.lombok.plugin)
}

version='0.0.1'


// --------------------------------------------------------------------------------
// Dependencies
// --------------------------------------------------------------------------------
<#if (project.isWebFlux())>
    <#include "SpringWebFluxDependencies.ftl">
<#elseif (project.isWebMvc())>
    <#include "SpringWebMvcDependencies.ftl">
<#elseif (project.isSpringBatch())>
    <#include "SpringBatchDependencies.ftl">
<#else>
    <#include "SpringBootDependencies.ftl">
</#if>

// --------------------------------------------------------------------------------
// Jib specific configuration for this application
// --------------------------------------------------------------------------------
jib {
    to {
        image = '${project.applicationName}'
        tags = [ 'latest' ]
    }
}

sonar {
    properties {
        property "sonar.projectName", "${project.applicationName}"
        property "sonar.projectKey", "${project.applicationName}"
        property "sonar.gradle.skipCompile", "true"
    }
}

/*
 * This task dependency is defined per module (subproject) since it cannot
 * be guaranteed that all modules of this application will have integration tests.
 * Thus, for any module that does not contain integration tests
 * (i.e., there's no `src/integrationTest/` folder), simply
 * remove the dependency on `integrationTestCodeCoverageReport`.
 */
tasks.named('check') {
    dependsOn tasks.named('integrationTestCodeCoverageReport')
    dependsOn tasks.named('testCodeCoverageReport')
}
