<#include "/common/Copyright.ftl">
package ${ControllerExceptionHandler.packageName()};

<#if endpoint.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${EntityResource.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ServiceApi.fqcn()};
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Verify exception handling
 */

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if endpoint.isWithTestContainers()>
@Import(ContainerConfiguration.class)
@Testcontainers
<#else>
@ExtendWith(SpringExtension.class)
</#if>
class ${ControllerExceptionHandler.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ${ServiceApi.className()} ${endpoint.entityVarName}Service;

    final ${SecureRandomSeries.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @Nested
    class ExceptionTests {

        @Test
        void shouldNotReturnStackTrace() throws Exception {
            // given
            given( ${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(any(String.class))).willThrow(new RuntimeException("Boom!"));
            given( ${endpoint.entityVarName}Service.update${endpoint.entityName}(any(${EntityResource.className()}.class))).willThrow(new RuntimeException("Bad data"));

            ${EntityResource.className()} payload = ${EntityResource.className()}.builder().resourceId(randomSeries.nextResourceId()).text("update me").build();

            // when/then
            mockMvc.perform(post("${endpoint.basePath}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(jsonPath("$.stackTrace").doesNotExist())
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andDo((print()))
                .andReturn();
        }
    }
}
