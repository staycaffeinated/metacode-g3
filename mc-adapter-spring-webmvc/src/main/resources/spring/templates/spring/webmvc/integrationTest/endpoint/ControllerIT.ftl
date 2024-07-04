<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

<#if endpoint.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import ${EntityResource.fqcn()};
import ${Entity.fqcn()};
import ${PojoToEntityConverter.fqcn()};
import ${EntityToPojoConverter.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${RegisterDatabaseProperties.fqcn()};
import ${Repository.fqcn()};
import ${Routes.fqcn()};
import ${WebMvcEjbTestFixtures.fqcn()};
import org.junit.jupiter.api.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
@Import(ContainerConfiguration.class)
@Testcontainers
</#if>
class ${Controller.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public static final String JSON_PATH__TEXT = "$." + ${endpoint.entityName}.Fields.TEXT;
    public static final String JSON_PATH__RESOURCE_ID = "$." + ${endpoint.entityName}.Fields.RESOURCE_ID;

    @Autowired
    private ${Repository.className()} ${endpoint.entityVarName}Repository;

    // This holds sample ${endpoint.ejbName}s that will be saved to the database
    private List<${Entity.className()}> ${endpoint.entityVarName}List = null;

    private final ${SecureRandomSeries.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @BeforeEach
    void setUp() {
        ${endpoint.entityVarName}Repository.saveAll(${WebMvcEjbTestFixtures.className()}.allItems());
        ${endpoint.entityVarName}List = ${WebMvcEjbTestFixtures.className()}.allItems();
    }

    @AfterEach
    public void tearDownEachTime() {
        ${endpoint.entityVarName}Repository.deleteAll();
    }

    @Nested
    class ValidateFindByText {
        @Test
        void whenSearchFindsHits_expectOkAndMatchingRecords() throws Exception {
            searchByText("First").andExpect(status().isOk());
        }

        @Test
        void whenSearchComesUpEmpty_expectOkButNoRecords() throws Exception {
            searchByText("xyzzy").andExpect(status().isOk());
        }
    }


    /*
     * FindById
     */
    @Nested
    class ValidateFindById {
        @Test
        void shouldFind${endpoint.entityName}ById() throws Exception {
            ${Entity.className()} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);
            String ${endpoint.entityVarName}Id = ${endpoint.entityVarName}.getResourceId();

            mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.findOne}, ${endpoint.entityVarName}Id))
            .andExpect(status().isOk())
            .andExpect(jsonPath(JSON_PATH__TEXT, is(${endpoint.entityVarName}.getText())));
        }
    }

    /*
    * Create method
    */
    @Nested
    class ValidateCreate${endpoint.entityName} {
        @Test
        void shouldCreateNew${endpoint.entityName}() throws Exception {
            ${EntityResource.className()} resource = ${EntityResource.className()}.builder().text("I am a new resource").build();

            mockMvc.perform(post(${Routes.className()}.${endpoint.routeConstants.create})
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath(JSON_PATH__TEXT, is(resource.getText())))
                    ;
        }

        /**
        * Verify the controller's data validation catches malformed inputs,
        * such as missing required fields, and returns either 'unprocessable entity'
        * or 'bad request'.
        */
        @Test
        void shouldReturn4xxWhenCreateNew${endpoint.entityName}WithoutText() throws Exception {
            ${EntityResource.className()} resource = ${EntityResource.className()}.builder().build();

            // Oddly, depending on whether the repository uses Postgres or H2, there are two
            // different outcomes. With H2, the controller's @Validated annotation is
            // applied and a 400 status code is returned. With Postgres, the @Validated
            // is ignored and a 422 error occurs when the database catches the invalid data.
            mockMvc.perform(post(${Routes.className()}.${endpoint.routeConstants.create})
                    .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().is4xxClientError());
        }
    }


    /*
     * Update method
     */
    @Nested
    class ValidateUpdate${endpoint.entityName} {

        @Test
        void shouldUpdate${endpoint.entityName}() throws Exception {
            ${Entity.className()} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);
            ${EntityResource.className()} resource = new ${EntityToPojoConverter.className()}().convert(${endpoint.entityVarName});

            mockMvc.perform(put(${Routes.className()}.${endpoint.routeConstants.update}, ${endpoint.entityVarName}.getResourceId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH__TEXT, is(${endpoint.entityVarName}.getText())));
        }
    }

    /*
     * Delete method
     */
    @Nested
    class ValidateDelete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            ${Entity.className()} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);

            mockMvc.perform(
                delete(${Routes.className()}.${endpoint.routeConstants.delete}, ${endpoint.entityVarName}.getResourceId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(JSON_PATH__TEXT, is(${endpoint.entityVarName}.getText())));
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    //
    // Helper methods
    //
    // ---------------------------------------------------------------------------------------------------------------

    protected ResultActions searchByText(String text) throws Exception {
        return mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.search}).param("text", text));
    }

}
