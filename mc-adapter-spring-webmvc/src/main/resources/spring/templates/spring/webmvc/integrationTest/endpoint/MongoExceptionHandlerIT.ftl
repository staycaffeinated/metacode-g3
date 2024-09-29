<#include "/common/Copyright.ftl">

package ${Controller.packageName()};

<#if (endpoint.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${ModelTestFixtures.fqcn()};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
<#if (endpoint.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
* Verify exception handling
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
<#if (endpoint.isWithTestContainers())>
@Import(ContainerConfiguration.class)
@Testcontainers
</#if>
class ${EntityResource.className()}ExceptionHandlingIT implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ${ServiceApi.className()} theService;

    @Nested
    class ExceptionTests {
        /**
        * Should the service happen to encounter an unchecked exception,
        * the stack trace must not be contained in the response.
        */
        @Test
        void shouldNotReturnStackTrace() throws Exception {
            // given: the service encounters an unchecked exception
            given(theService.find${endpoint.entityName}ByResourceId(any(String.class))).willThrow(new RuntimeException("This is a test"));
            given(theService.update${endpoint.entityName}(any(${endpoint.entityName}.class))).willThrow(new RuntimeException("Bad data"));

            ${endpoint.pojoName} payload = ${ModelTestFixtures.className()}.oneWithResourceId();

            // when/then
            // @formatter:off
            mockMvc.perform(post("${endpoint.basePath}").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(jsonPath("$.stackTrace").doesNotExist())
                    .andExpect(jsonPath("$.trace").doesNotExist())
                    .andDo((print())).andReturn();
            // @formatter:on
        }
    }
}

