<#include "/common/Copyright.ftl">
package ${ControllerExceptionHandler.packageName()};

<#if endpoint.isWithTestContainers()>
import ${AbstractPostgresIntegrationTest.fqcn()};
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${EntityCommandUseCase.fqcn()};
import ${EntityQueryUseCase.fqcn()};
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Verify exception handling
 */

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
@Testcontainers
class ${ControllerExceptionHandler.integrationTestClass()} extends ${AbstractPostgresIntegrationTest.className()} {
<#else>
@ExtendWith(SpringExtension.class)
class ${ControllerExceptionHandler.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
</#if>
    @Autowired
    MockMvcTester mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    @MockitoBean
    private ${EntityCommandUseCase.className()} commandUseCase;

    @MockitoBean
    private ${EntityQueryUseCase.className()} queryUseCase;

    final ${SecureRandomSeries.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @Nested
    class ExceptionTests {

        @Test
        void shouldNotReturnStackTrace() throws Exception {
            // given
            given( queryUseCase.find${endpoint.entityName}ByResourceId(any(String.class))).willThrow(new RuntimeException("Boom!"));
            given( commandUseCase.update${endpoint.entityName}(any(${EntityResource.className()}.class))).willThrow(new RuntimeException("Bad data"));

            ${EntityResource.className()} payload = ${EntityResource.className()}.builder().resourceId(randomSeries.nextResourceId()).text("update me").build();

            // when/then
            mockMvc.perform(post("${endpoint.basePath}")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(payload)))
                    .assertThat()
                    .hasStatus4xxClientError()
                    .hasContentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                    .bodyJson()
                    .doesNotHavePath("$.stackTrace")
                    .doesNotHavePath("$.trace");
        }
    }
}
