<#include "/common/Copyright.ftl">
package ${Application.packageName()};



import ${RegisterDatabaseProperties.fqcn()};
<#-- ========================= -->
<#-- Postgres & TestContainers -->
<#-- ========================= -->
<#if project.isWithPostgres() && project.isWithTestContainers()>
import ${AbstractPostgresIntegrationTest.fqcn()};
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static ${SpringProfiles.fqcn()}.INTEGRATION_TEST;
import static ${SpringProfiles.fqcn()}.TEST;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({TEST,INTEGRATION_TEST})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class ${Application.integrationTestClass()} extends ${AbstractPostgresIntegrationTest.className()} {
<#else>
<#-- ========================= -->
<#-- Vanilla                   -->
<#-- ========================= -->
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ${Application.integrationTestClass()} implements RegisterDatabaseProperties {
</#if>

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }
}
