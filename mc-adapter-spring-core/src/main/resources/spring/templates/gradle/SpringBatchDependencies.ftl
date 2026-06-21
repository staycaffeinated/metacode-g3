
/*
 * Until integration tests are written, a build will fail because no integration
 * tests are discovered. The code generator cannot generate integration tests because
 * it has no idea what ItemReaders/ItemProcessors/ItemWriters will be implemented,
 * nor what order the Steps will be in.  On the other hand, we want the directory
 * structure and a placeholder test to be available as a starting point. To have the
 * placeholder while also avoiding a build failure, the `failOnNoDiscoveredTests`
 * flag is set to `false`.  Once integration tests are written, `failOnNoDiscoveredTests`
 * should be set to `true`, or just remove this block.
 */
integrationTest {
    failOnNoDiscoveredTests = false
}


dependencies {
    // enable Mockito Java Agents
    <#noparse>mockitoAgent("org.mockito:mockito-core") { transitive = false }</#noparse>

    annotationProcessor libs.spring.boot.config.processor

    developmentOnly libs.spring.devtools

    implementation libs.spring.boot.starter.web
    implementation libs.spring.boot.starter.batch
    implementation libs.spring.boot.starter.data.jpa
<#if (project.isWithKafka())>
    implementation libs.spring.boot.starter.kafka
</#if>

<#if (project.isWithPostgres())>
    runtimeOnly libs.postgresql
</#if>
    // Optional: This reports out-of-date property names
    runtimeOnly libs.spring.boot.properties.migrator

<#if (project.isWithTestContainers())>
    testImplementation libs.spring.boot.testcontainers
    testImplementation libs.testcontainers.jupiter
    <#if (project.isWithPostgres())> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testcontainers.postgres
    </#if>
</#if>

    testImplementation libs.spring.boot.starter.test
    testImplementation libs.junit.jupiter
    testImplementation libs.spring.batch.test
<#if (project.isWithKafka())>
    testImplementation libs.spring.kafka.test
</#if>
}