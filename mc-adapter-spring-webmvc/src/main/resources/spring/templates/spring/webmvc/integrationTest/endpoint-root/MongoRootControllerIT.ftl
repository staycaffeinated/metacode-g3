<#include "/common/Copyright.ftl">
package ${RootController.packageName()};

<#if (project.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
<#if (project.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
<#if (project.isWithTestContainers())>
@Import(ContainerConfiguration.class)
@Testcontainers
</#if>
class RootControllerIT implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}