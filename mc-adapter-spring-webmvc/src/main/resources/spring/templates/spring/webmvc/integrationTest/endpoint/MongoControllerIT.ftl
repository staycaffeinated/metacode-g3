<#include "/common/Copyright.ftl">

package ${Controller.packageName()};

<#if (endpoint.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${EntityResource.fqcn()};
import ${ModelTestFixtures.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${Document.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${DocumentToPojoConverter.fqcn()};
import ${Repository.fqcn()};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.testcontainers.junit.jupiter.Testcontainers;
<#if (endpoint.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
<#if (endpoint.isWithTestContainers())>
@Import(ContainerConfiguration.class)
@Testcontainers
</#if>
class ${Controller.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ${Repository.className()} repository;

    private List<${Document.className()}> documentList;


    @BeforeEach
    void setUp() {
        documentList = ${DocumentTestFixtures.className()}.allItems();
        repository.saveAll(${DocumentTestFixtures.className()}.allItems());
    }

    @AfterEach
    void tearDownEachTime() {
        repository.deleteAll();
    }

    @Nested
    class ValidateFindByText {
        @Test
        void whenSearchFindsHits_expectOkAndMatchingRecords() throws Exception {
            searchByText("Bluey").assertThat().hasStatus(HttpStatus.OK);
        }

        @Test
        void whenSearchComesUpEmpty_expectOkButNoRecords() throws Exception {
            searchByText("xyzzy").assertThat().hasStatus(HttpStatus.OK);
        }
    }

    /*
     * FindById
     */
    @Nested
    class ValidateFindById {
        @Test
        void shouldFind${endpoint.entityName}ById() throws Exception {
            ${Document.className()} item = documentList.get(0);

            findOne(item.getResourceId()).assertThat().hasStatus(HttpStatus.OK);
        }
    }

    /*
    * Create method
    */
    @Nested
    class ValidateCreate${endpoint.pojoName} {
        @Test
        void shouldCreateNew${endpoint.pojoName}() throws Exception {
            ${EntityResource.className()} resource = ${ModelTestFixtures.className()}.oneWithoutResourceId();

            createOne(resource).assertThat().hasStatus(HttpStatus.CREATED);
        }

        /**
         * Verify the controller's data validation catches malformed inputs, such as
         * missing required fields, and returns either 'unprocessable entity' or 'bad
         * request'.
         */
        @Test
        void shouldReturn201WhenCreateNew${endpoint.pojoName}WithoutText() throws Exception {
            ${EntityResource.className()} resource = ${EntityResource.className()}.builder().build();

            createOne(resource).assertThat().hasStatus(HttpStatus.CREATED);
        }
    }

    /*
    * Update method
    */
    @Nested
    class ValidateUpdate${endpoint.entityName} {

        @Test
        @SuppressWarnings("all")
        void shouldUpdate${endpoint.entityName}() throws Exception {
            ${Document.className()} doc = documentList.get(0);
            ${EntityResource.className()} modified = new ${DocumentToPojoConverter.className()}().convert(doc);
            modified.setText("modified");

            updateOne(modified)
                    .assertThat()
                    .hasStatus(HttpStatus.OK)
                    .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                    .bodyJson()
                    .extractingPath("$")
                    .convertTo(InstanceOfAssertFactories.list(Widget.class))
                    .hasSize(1)
                    .satisfies(list -> assertThat(list.stream().map(${EntityResource.className()}::getResourceId)).isNotEmpty())
                    .satisfies(list -> assertThat(list.stream().map(${EntityResource.className()}::getText)).isNotEmpty());
        }
    }

    /*
     * Delete method
     */
    @Nested
    class ValidateDelete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            ${Document.className()} document = documentList.get(0);

            deleteOne(document.getResourceId()).assertThat().hasStatus(HttpStatus.OK);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    //
    // Helper methods
    //
    // ---------------------------------------------------------------------------------------------------------------

    protected MvcTestResult searchByText(String text) throws Exception {
        return mockMvcTester.get()
                            .uri(${Routes.className()}.${endpoint.routeConstants.search})
                            .param(${endpoint.entityName}.Fields.TEXT, text)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .exchange();
    }

    protected MvcTestResult findOne(String resourceId) throws Exception {
        return mockMvcTester.get()
                            .uri(${Routes.className()}.${endpoint.routeConstants.findOne}, resourceId)
                            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .exchange();
    }

    protected MvcTestResult createOne(${endpoint.entityName} pojo) throws Exception {
        return mockMvcTester.post()
                            .uri(${Routes.className()}.${endpoint.routeConstants.create})
                            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pojo))
                            .exchange();
    }

    protected MvcTestResult updateOne(${endpoint.entityName} pojo) throws Exception {
        return mockMvcTester.put()
                            .uri(${Routes.className()}.${endpoint.routeConstants.update}, pojo.getResourceId())
                            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pojo))
                            .exchange();
    }

    protected MvcTestResult deleteOne(String resourceId) throws Exception {
        return mockMvcTester.delete()
                            .uri(${Routes.className()}.${endpoint.routeConstants.delete}, resourceId)
                            .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_PROBLEM_JSON)
                            .exchange();
    }
}
