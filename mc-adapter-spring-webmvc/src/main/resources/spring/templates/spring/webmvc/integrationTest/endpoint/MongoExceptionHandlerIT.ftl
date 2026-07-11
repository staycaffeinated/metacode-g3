<#include "/common/Copyright.ftl">

package ${ControllerExceptionHandler.packageName()};

<#if (endpoint.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${ModelTestFixtures.fqcn()};
import ${ServiceApi.fqcn()};

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.junit.jupiter.Testcontainers;
<#if (endpoint.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
</#if>
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * Verify exception handling
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
<#if (endpoint.isWithTestContainers())>
@Import(ContainerConfiguration.class)
@Testcontainers
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class
})
</#if>
class ${ControllerExceptionHandler.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    JsonMapper jsonMapper;

    @MockitoBean
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
            mockMvcTester.perform(post(${Routes.className()}.${endpoint.routeConstants.create}).contentType(MediaType.APPLICATION_JSON)
                         .content(jsonMapper.writeValueAsString(payload)))
                         .assertThat()
                         .hasStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                         .bodyJson()
                         .doesNotHavePath("$.stackTrace")
                         .doesNotHavePath("$.trace");
            // @formatter:on
        }
    }
}

