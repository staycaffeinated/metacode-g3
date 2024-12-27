<#include "/common/Copyright.ftl">
package ${RootController.packageName()};

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

<#-- ======================================= -->
<#-- When using Postgres with TestContainers -->
<#-- ======================================= -->
<#if project.isWithTestContainers()>
import ${AbstractPostgresIntegrationTest.fqcn()};
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.context.annotation.Import;
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if project.isWithTestContainers()>
@Testcontainers
class ${RootController.integrationTestClass()} extends ${AbstractPostgresIntegrationTest.className()} {
<#else>
class ${RootController.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
</#if>
    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}