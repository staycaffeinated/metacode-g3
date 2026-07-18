plugins {
    id 'buildlogic.application-conventions'
<#if (!project.isSpringBoot())>
    id 'buildlogic.integration-test'
</#if>
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
    dockerClient {
        executable = project.findProperty('dockerExecutable') ?: '/usr/local/bin/docker'
    }
    from {
        // Any java version compatible with Spring Boot 4.x should work
        // To run on Windows use, for example: 'eclipse-temurin:21-jre-windowsservercore'
        image = 'eclipse-temurin:21-jre-jammy'
        platforms {
            platform {
                os = 'linux'
                architecture = 'arm64'
           }
           platform {
                os = 'linux'
                architecture = 'amd64'
           }
        }
    }
    to {
        image = '${project.applicationName}'
        tags = [ 'latest', version ]
    }
    container {
        format = 'OCI'
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
<#if (!project.isSpringBoot())>
    dependsOn tasks.named('integrationTestCodeCoverageReport')
</#if>
    dependsOn tasks.named('testCodeCoverageReport')
}
