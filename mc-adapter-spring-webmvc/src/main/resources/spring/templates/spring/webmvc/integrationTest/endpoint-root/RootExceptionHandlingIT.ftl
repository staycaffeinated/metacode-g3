<#include "/common/Copyright.ftl">

package ${RootControllerExceptionHandler.packageName()};

<#-- ============================================================================== -->
<#-- When using Postgres with TestContainers                                        -->
<#-- This can be confusing to read because we have to blocks of import statements   -->
<#-- The Palantir checkStyle complains if the imports are not clumped together,     -->
<#-- and it struggles (errors) when trying to reorganize them w/o the help of an IDE. -->
<#-- Tech Debt: refactor maybe into 2 distinct tempates                             -->
<#-- ============================================================================== -->
<#if project.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.context.annotation.Import;
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${RootController.fqcn()};
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
* Integration tests of the exception handling of the root controller
*/
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if project.isWithTestContainers()>
@Import(ContainerConfiguration.class)
@Testcontainers
<#else>
@ExtendWith(SpringExtension.class)
</#if>
class ${RootControllerExceptionHandler.className()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ${RootController.className()} controllerUnderTest;

    @Nested
    class ExceptionTests {

        @Test
        void shouldNotReturnStackTrace() throws Exception {
            // when/then
            mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stackTrace").doesNotExist()) // sometimes traces come back like this
                .andExpect(jsonPath("$.trace").doesNotExist()) // sometimes traces come back like this
                .andDo((print())).andReturn();
        }

        /**
         * This exercises the GlobalExceptionHandler by simulating an HttpRequestMethodNotSupportedException.
         * If properly configured, the ProblemHandling method that handles this exception should catch it
         * and configure a proper problem/json response.  The point of this test is not to validate ProblemHandling,
         * but to ensure these kinds of exceptions return a problem/json response.
         */
        @Test
        void onHttpRequestMethodNotSupported() throws Exception {
            // For this test, let's say http GET is not allowed...
            // Note: Mockito won't allow its <code>thenThrows</code> method to throw this particular exception.
            // When attempted, Mockito gives the error "Checked exception is invalid for this method".
            // The work-around is to use the <code>willAnswer</code> method.
            given(controllerUnderTest.getHome()).willAnswer(invocation -> {
                        throw new HttpRequestMethodNotSupportedException("GET");
                        });

            // when/then
            mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andDo(print()).andReturn();
        }
    }
}
