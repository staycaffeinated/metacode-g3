<#include "/common/Copyright.ftl">
package ${Application.packageName()};

import ${RegisterDatabaseProperties.fqcn()};
<#-- ========================= -->
<#-- Postgres & TestContainers -->
<#-- ========================= -->
<#if project.isWithPostgres() && project.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static ${SpringProfiles.fqcn()}.INTEGRATION_TEST;
import static ${SpringProfiles.fqcn()}.TEST;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles({TEST,INTEGRATION_TEST})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ContainerConfiguration.class)
@Testcontainers
class ApplicationTests implements RegisterDatabaseProperties {
<#else>
<#-- ========================= -->
<#-- Vanilla                   -->
<#-- ========================= -->
import org.junit.jupiter.api.Test;

class ApplicationTests implements RegisterDatabaseProperties {
</#if>

    @Test
    @SuppressWarnings("java:S2699") // there's nothing to assert
    void contextLoads() {
        // If this test runs without throwing an exception, then SpringBoot started successfully
    }
}
