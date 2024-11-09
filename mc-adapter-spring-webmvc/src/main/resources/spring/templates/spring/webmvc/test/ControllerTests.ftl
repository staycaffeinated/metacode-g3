<#include "/common/Copyright.ftl">

package ${Controller.packageName()};

import ${Entity.fqcn()};
import ${EntityResource.fqcn()};
import ${ModelTestFixtures.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.TransactionSystemException;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(${Controller.className()}.class)
@ActiveProfiles("test")
class ${Controller.testClass()} {

    public static final String PATH_TO_TEXT = "$." + ${EntityResource.className()}.Fields.TEXT;
    public static final String PATH_TO_RESOURCE_ID = "$." + ${EntityResource.className()}.Fields.RESOURCE_ID;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ${ServiceApi.className()} ${endpoint.entityVarName}Service;

    @Autowired
    private ObjectMapper objectMapper;

    private List<${endpoint.pojoName}> ${endpoint.entityVarName}List;
    private Page<${endpoint.pojoName}> pageOfData;

    private final ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @BeforeEach
    void setUp() {
        ${endpoint.entityVarName}List = ${ModelTestFixtures.className()}.allItems();

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());

        pageOfData = new PageImpl<>(${endpoint.entityVarName}List);
    }

    @AfterEach
    void tearDownEachTime() {
        reset ( ${endpoint.entityVarName}Service );
    }

    @Nested
    class FindAllTests {
        /*
         * shouldFetchAll${endpoint.entityName}s
         */
        @Test
        void shouldFetchAll${endpoint.entityName}s() throws Exception {
            int expectedSize = ${ModelTestFixtures.className()}.allItems().size();
            given(${endpoint.entityVarName}Service.findAll${endpoint.entityName}s()).willReturn(${ModelTestFixtures.className()}.allItems());

            findAllEntities()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(expectedSize)));
        }
    }

    @Nested
    class FindByIdTests {
        /*
         *  shouldFind${endpoint.entityName}ById
         */
        @Test
        void shouldFind${endpoint.entityName}ById() throws Exception {
            // given
            ${endpoint.pojoName} ${endpoint.entityVarName} = ${ModelTestFixtures.className()}.oneWithResourceId();
            String resourceId = ${endpoint.entityVarName}.getResourceId();

            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId( resourceId ))
                .willReturn(Optional.of(${endpoint.entityVarName}));

            // when/then
            findSpecificEntity(resourceId)
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_TO_TEXT, is(${endpoint.entityVarName}.getText())))
                ;
        }

        @Test
        void shouldReturn404WhenFetchingNonExisting${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId( resourceId )).willReturn(Optional.empty());

            // when/then
            findSpecificEntity(resourceId).andExpect(status().isNotFound());
        }
    }

    @Nested
    class Create${endpoint.entityName}Tests {
        @Test
        void shouldCreateNew${endpoint.entityName}() throws Exception {
            // given
            ${endpoint.pojoName} resourceBeforeSave = ${ModelTestFixtures.className()}.oneWithoutResourceId();
            ${endpoint.pojoName} resourceAfterSave = ${ModelTestFixtures.className()}.copyOf(resourceBeforeSave);
            resourceAfterSave.setResourceId(randomSeries.nextResourceId());
            given(${endpoint.entityVarName}Service.create${endpoint.entityName}( any(${endpoint.pojoName}.class))).willReturn(resourceAfterSave);

            // when/then
            createEntity(resourceBeforeSave).andExpect(status().isCreated())
                .andExpect(jsonPath(PATH_TO_RESOURCE_ID, notNullValue()))
                .andExpect(jsonPath(PATH_TO_TEXT, is(resourceAfterSave.getText())));
        }

        @Test
        void whenDatabaseThrowsException_expectUnprocessableEntityResponse() throws Exception {
            // given the database throws an exception when the entity is saved
            given(${endpoint.entityVarName}Service.create${endpoint.entityName}( any(${endpoint.pojoName}.class))).willThrow(TransactionSystemException.class);
            ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().build();

            createEntity(resource).andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    class Update${endpoint.entityName}Tests {
        @Test
        void shouldUpdate${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            ${endpoint.pojoName} ${endpoint.entityVarName} = ${EntityResource.className()}.builder().resourceId(resourceId).text("sample text").build();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.of(${endpoint.entityVarName}));
            given(${endpoint.entityVarName}Service.update${endpoint.entityName}(any(${endpoint.pojoName}.class))).willReturn(Optional.of(${endpoint.entityVarName}));

            // when/then
            updateEntity(${endpoint.entityVarName}).andExpect(status().isOk())
                .andExpect(jsonPath(PATH_TO_TEXT, is(${endpoint.entityVarName}.getText())));
        }

        @Test
        void shouldReturn404WhenUpdatingNonExisting${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.empty());

            // when/then
            ${endpoint.pojoName} resource = ${EntityResource.className()}.builder().resourceId(resourceId).text("updated text").build();

            // Attempt to update an entity that does not exist
            updateEntity(resource).andExpect(status().isNotFound());
        }

        /**
         * When the Ids in the query string and request body do not match, expect
         * an 'Unprocessable Entity' response code
         */
        @Test
        void shouldReturn422WhenIdsMismatch() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            String mismatchingId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.empty());

            // when the ID in the request body does not match the ID in the query string...
            ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().resourceId(mismatchingId).text("updated text").build();

            // Submit an update request, with the ID in the URL not matching the ID in the body.
            // Expect back an UnprocessableEntity status code
            updateEntity(resourceId, resource).andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    class Delete${endpoint.entityName}Tests {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            // given
            ${endpoint.pojoName} ${endpoint.entityVarName} = ${ModelTestFixtures.className()}.oneWithResourceId();
            String resourceId = ${endpoint.entityVarName}.getResourceId();

            // Mock the service layer finding the resource being deleted
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.of(${endpoint.entityVarName}));
            doNothing().when(${endpoint.entityVarName}Service).delete${endpoint.entityName}ByResourceId(${endpoint.entityVarName}.getResourceId());

            // when/then
            deleteEntity(resourceId).andExpect(status().isOk())
            .andExpect(jsonPath(PATH_TO_TEXT, is(${endpoint.entityVarName}.getText())));
        }

        @Test
        void shouldReturn404WhenDeletingNonExisting${endpoint.entityName}() throws Exception {
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.empty());

            deleteEntity(resourceId).andExpect(status().isNotFound());
        }
    }

    @Nested
    class SearchByTextTests {
        @Test
        @SuppressWarnings("unchecked")
        void shouldReturnListWhenMatchesAreFound() throws Exception {
            given (${endpoint.entityVarName}Service.findByText(any(String.class), any(Pageable.class))).willReturn(pageOfData);

            // when/then (the default Pageable in the controller is sufficient for testing)
            searchByText("text").andExpect(status().isOk());
        }
    }

    @Nested
    class SearchTextValidationTests {
        @Test
        void whenTextIsTooLong_expectError() throws Exception {
            searchByText("supercalifragilisticexpialidocious").andExpect(status().is4xxClientError());
        }

        @Test
        void whenTextContainsInvalidCharacters_expectError() throws Exception {
            searchByText("192.168.0.0<555").andExpect(status().is4xxClientError());
        }
    }

    @Nested
    class SearchUseCases {
        @Test
        void shouldReturnSomethingFromQuery() throws Exception {
            given (${endpoint.entityVarName}Service.search(any(String.class), any(Pageable.class))).willReturn(pageOfData);
            search("text!=Bluey").andExpect(status().isOk());
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    //
    // Helper methods
    //
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Sends a findAll request
     */
    protected ResultActions findAllEntities() throws Exception {
        return mockMvc.perform(get( ${Routes.className()}.${endpoint.routeConstants.findAll} ));
    }

    /**
     * Sends a findOne request
     */
    protected ResultActions findSpecificEntity(String resourceId) throws Exception {
        return mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.findOne}, resourceId ));
    }

    
    /**
     * Submits a findByProperty request
     */
    protected ResultActions searchByText(String text) throws Exception {
        return mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.findByProperty}).param("text", text));
    }

    /**
     * Submits an RSQL search request
     */
    protected ResultActions search(String rsqlQuery) throws Exception {
        return mockMvc.perform(get(${Routes.className()}.${endpoint.routeConstants.search}).param("q", rsqlQuery));
    }


    /**
     * Submits a Delete request
     */
    protected ResultActions deleteEntity(String resourceId) throws Exception {
        return mockMvc.perform(delete(${Routes.className()}.${endpoint.routeConstants.delete},resourceId));
    }

    /**
     * To support the use case of a well-formed update request
     */
    protected ResultActions updateEntity(${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvc.perform(put(${endpoint.entityName}Routes.${endpoint.routeConstants.update}, ${endpoint.entityVarName}.getResourceId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(${endpoint.entityVarName})));
    }

    /**
    * To support the use case of the ID in the query string not matching the ID in the payload
    */
    protected ResultActions updateEntity(String resourceId, ${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvc.perform(put(${endpoint.entityName}Routes.${endpoint.routeConstants.update}, resourceId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(${endpoint.entityVarName})));
    }

    /**
    * Submits a Post request
    */
    protected ResultActions createEntity(${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvc.perform(post(${endpoint.entityName}Routes.${endpoint.routeConstants.create})
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(${endpoint.entityVarName})));
    }
}
