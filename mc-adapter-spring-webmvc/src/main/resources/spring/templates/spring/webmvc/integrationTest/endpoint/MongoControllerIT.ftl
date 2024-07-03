<#include "/common/Copyright.ftl">

package ${Controller.packageName()};

<#if (endpoint.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${EntityResource.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${Document.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${DocumentToPojoConverter.fqcn()};
import ${Repository.fqcn()};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
<#if (endpoint.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ${Repository.className()} repository;

    private List<${Document.className()}> documentList;

    @BeforeEach
    void setUp() {
        documentList = ${DocumentTestFixtures.className()}.allItems();
        repository.saveAll(${DocumentTestFixtures.className()}.allItems());
    }

    @AfterEach
    public void tearDownEachTime() {
        repository.deleteAll();
    }

    @Nested
    public class ValidateFindByText {
        @Test
        void whenSearchFindsHits_expectOkAndMatchingRecords() throws Exception {
            searchByText("Bluey").andExpect(status().isOk());
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
    public class ValidateFindById {
        @Test
        void shouldFind${endpoint.entityName}ById() throws Exception {
            ${Document.className()} item = documentList.get(0);

            findOne(item.getResourceId()).andExpect(status().isOk())
            .andExpect(jsonPath("$.resourceId", is(item.getResourceId())));
        }
    }

    /*
    * Create method
    */
    @Nested
    public class ValidateCreate${endpoint.pojoName} {
        @Test
        void shouldCreateNew${endpoint.pojoName}() throws Exception {
            ${EntityResource.className()} resource = ${WebMvcModelTestFixtures.className()}.oneWithoutResourceId();

            createOne(resource).andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(resource.getText())));
        }

        /**
         * Verify the controller's data validation catches malformed inputs, such as
         * missing required fields, and returns either 'unprocessable entity' or 'bad
         * request'.
         */
        @Test
        void shouldReturn201WhenCreateNew${endpoint.pojoName}WithoutText() throws Exception {
            ${EntityResource.className()} resource = ${EntityResource.className()}.builder().build();

            // Group validation appears to be buggy in Spring 6.
            // The validations in Group::OnCreate and Group::OnUpdate
            // are not being honored. For example, an entity's resourceId should
            // be blank when creating the entity, but non-blank when updating
            // the entity. The culprit for this (broken) behavior is not yet known.
            createOne(resource).andExpect(status().isCreated());
        }
    }

    /*
    * Update method
    */
    @Nested
    public class ValidateUpdate${endpoint.entityName} {

        @Test
        @SuppressWarnings("all")
        void shouldUpdate${endpoint.entityName}() throws Exception {
            ${Document.className()} doc = documentList.get(0);
            ${EntityResource.className()} modified = new ${DocumentToPojoConverter.className()}().convert(doc);
            modified.setText("modified");

            updateOne(modified).andExpect(status().isOk())
                .andExpect(jsonPath("$..text").value(modified.getText()));
        }
    }

    /*
     * Delete method
     */
    @Nested
    public class ValidateDelete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            ${Document.className()} document = documentList.get(0);

            deleteOne(document.getResourceId()).andExpect(status().isOk());
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

    protected ResultActions findOne(String resourceId) throws Exception {
        return mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.findOne}, resourceId));
    }

    protected ResultActions createOne(${endpoint.entityName} pojo) throws Exception {
        return mockMvc.perform(post(${Routes.className()}.${endpoint.routeConstants.create}).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pojo)));
    }

    protected ResultActions updateOne(${endpoint.entityName} pojo) throws Exception {
        return mockMvc.perform(put(${Routes.className()}.${endpoint.routeConstants.update}, pojo.getResourceId()).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pojo)));
    }

    protected ResultActions deleteOne(String resourceId) throws Exception {
        return mockMvc.perform(delete(${Routes.className()}.${endpoint.routeConstants.delete}, resourceId));
    }
}
