dependencies {
annotationProcessor libs.springBootConfigProcessor

developmentOnly libs.springDevTools

implementation libs.springBootStarterWeb
implementation libs.springBootStarterBatch
implementation libs.springBootStarterDataJpa

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
</#if>
// Optional: This reports out-of-date property names
runtimeOnly libs.springBootPropertiesMigrator

<#if (project.isWithTestContainers())>
    testImplementation platform( libs.testContainersBom )
    testImplementation libs.testContainersJupiter
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
        testImplementation libs.testContainersPostgres
    </#if>
</#if>

testImplementation (libs.springBootStarterTest)
testImplementation (platform( libs.junitBillOfMaterial ))
testImplementation (libs.junitJupiter)
testImplementation libs.springBatchTest
}