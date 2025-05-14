<#include "/common/Copyright.ftl">

package ${Controller.packageName()};

import ${Document.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${EntityResource.fqcn()};
import ${PojoTestFixtures.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;

@WebMvcTest(${Controller.className()}.class)
@ActiveProfiles("test")
class r${Controller.testClass()} {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private ${ServiceApi.className()} ${endpoint.entityVarName}Service;

    @Autowired
    private ObjectMapper objectMapper;

    private List<${EntityResource.className()}> ${endpoint.entityVarName}List;
    private Page<${EntityResource.className()}> pageOfData;

    private final ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @BeforeEach
    void configureSystemUnderTest() {
        ${endpoint.entityVarName}Service = Mockito.mock(${ServiceApi.className()}.class);
        var mockMvc = MockMvcBuilders.standaloneSetup(new ${Controller.className()}(${endpoint.entityVarName}Service))
                                     .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                                     .build();
        mockMvcTester = MockMvcTester.create(mockMvc);

        ${endpoint.entityVarName}List = ${PojoTestFixtures.className()}.allItems();
        pageOfData = new PageImpl<>(${endpoint.entityVarName}List);
    }

    @AfterEach
    void resetSystemUnderTest() {
        reset ( ${endpoint.entityVarName}Service );
    }

    @Nested
    class FindAllTests {
        /*
        * shouldFetchAll${endpoint.entityName}s
        */
        @Test
        void shouldFetchAll${endpoint.entityName}s() throws Exception {
            int expectedSize = ${PojoTestFixtures.className()}.allItems().size();
            given(${endpoint.entityVarName}Service.findAll${endpoint.entityName}s()).willReturn(${PojoTestFixtures.className()}.allItems());

            var jsonPathToId = "$.[0]." + ${endpoint.entityName}.Fields.RESOURCE_ID;

            findAllEntities().assertThat()
                             .hasStatus(HttpStatus.OK)
                             .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                             .bodyJson()
                             .hasPathSatisfying(jsonPathToId, path -> assertThat(path).isNotEmpty());

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
            ${EntityResource.className()} ${endpoint.entityVarName} = ${PojoTestFixtures.className()}.oneWithResourceId();
            String resourceId = ${endpoint.entityVarName}.getResourceId();

            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId( resourceId ))
                .willReturn(Optional.of(${endpoint.entityVarName}));

            // when/then
            findSpecificEntity(resourceId).assertThat()
                                          .hasStatus(HttpStatus.OK)
                                          .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                                          .bodyJson()
                                          .extractingPath("$")
                                          .asMap()
                                          .containsEntry(Widget.Fields.RESOURCE_ID, resourceId)
                                          .containsEntry(Widget.Fields.TEXT, widget.getText());
        }

        @Test
        void shouldReturn404WhenFetchingNonExisting${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId( resourceId ))
                .willReturn(Optional.empty());

            // when/then
            findSpecificEntity(resourceId).assertThat().hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class Create${endpoint.entityName}Tests {
        @Test
        void shouldCreateNew${endpoint.entityName}() throws Exception {
            // given
            ${EntityResource.className()} resourceBeforeSave = ${PojoTestFixtures.className()}.oneWithoutResourceId();
            ${EntityResource.className()} resourceAfterSave = ${PojoTestFixtures.className()}.copyOf(resourceBeforeSave);
            resourceAfterSave.setResourceId(randomSeries.nextResourceId());
            given(${endpoint.entityVarName}Service.create${endpoint.entityName}( any(${endpoint.pojoName}.class))).willReturn(resourceAfterSave);

            // when/then
            createEntity(resourceBeforeSave).assertThat()
                                            .hasStatus(HttpStatus.CREATED)
                                            .bodyJson()
                                            .extractingPath("$")
                                            .asMap()
                                            .containsEntry(${EntityResource.className()}.Fields.TEXT, resourceAfterSave.getText())
                                            .containsKey(${EntityResource.className()}.Fields.RESOURCE_ID);
        }

        @Test
        void whenDatabaseThrowsException_expectUnprocessableEntityResponse() throws Exception {
            // given the database throws an exception when the entity is saved
            given(${endpoint.entityVarName}Service.create${endpoint.entityName}( any(${endpoint.pojoName}.class))).willThrow(TransactionSystemException.class);
            ${EntityResource.className()} resource = ${EntityResource.className()}.builder().build();

            createEntity(resource).assertThat().hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Nested
    class Update${endpoint.entityName}Tests {
        @Test
        void shouldUpdate${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            ${endpoint.pojoName} ${endpoint.entityVarName} = ${endpoint.pojoName}.builder().resourceId(resourceId).text("sample text").build();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.of(${endpoint.entityVarName}));
            given(${endpoint.entityVarName}Service.update${endpoint.entityName}(any(${endpoint.pojoName}.class))).willReturn(List.of(${endpoint.entityVarName}));

            // when/then
            updateEntity(resourceId, widget).assertThat().hasStatus(HttpStatus.OK);
        }

        @Test
        void shouldReturn404WhenUpdatingNonExisting${endpoint.entityName}() throws Exception {
            // given
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.empty());

            // when/then
            ${endpoint.pojoName} resource = ${endpoint.pojoName}.builder().resourceId(resourceId).text("updated text").build();

            // Attempt to update an entity that does not exist
            updateEntity(resource).assertThat().hasStatus(HttpStatus.NOT_FOUND);
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
            updateEntity(resourceId, resource).assertThat().hasStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Nested
    class Delete${endpoint.entityName}Tests {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            // given
            ${endpoint.pojoName} ${endpoint.entityVarName} = ${endpoint.entityName}TestFixtures.oneWithResourceId();
            String resourceId = ${endpoint.entityVarName}.getResourceId();

            // Mock the service layer finding the resource being deleted
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.of(${endpoint.entityVarName}));
            doNothing().when(${endpoint.entityVarName}Service).delete${endpoint.entityName}ByResourceId(${endpoint.entityVarName}.getResourceId());

            // when/then
            deleteEntity(resourceId).assertThat()
                                    .hasStatus(HttpStatus.OK)
                                    .bodyJson()
                                    .extractingPath("$")
                                    .asMap()
                                    .containsKey(${EntityResource.className()}.Fields.RESOURCE_ID);
        }

        @Test
        void shouldReturn404WhenDeletingNonExisting${endpoint.entityName}() throws Exception {
            String resourceId = randomSeries.nextResourceId();
            given(${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(resourceId)).willReturn(Optional.empty());

            deleteEntity(resourceId).assertThat().hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class SearchByTextTests {
        @Test
        @SuppressWarnings("unchecked")
        void shouldReturnListWhenMatchesAreFound() throws Exception {
            given (${endpoint.entityVarName}Service.findByText(any(String.class), any(Pageable.class))).willReturn(pageOfData);

            // when/then (the default Pageable in the controller is sufficient for testing)
            searchByText("some%20bvalue").assertThat().hasStatus(HttpStatus.OK);
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
    protected MvcTestResult findAllEntities() throws Exception {
        return mockMvcTester.get()
                            .uri(${endpoint.entityName}Routes.${endpoint.routeConstants.findAll} )
                            .exchange();
    }

    /**
     * Sends a findOne request
     */
    protected MvcTestResult findSpecificEntity(String resourceId) throws Exception {
        return mockMvcTester.get()
                            .uri(${endpoint.entityName}Routes.${endpoint.routeConstants.findOne}, resourceId)
                            .exchange();
    }


    /**
     * Submits a search request
     */
    protected MvcTestResult searchByText(String text) {
        return mockMvcTester.get()
                            .uri(${endpoint.entityName}Routes.${endpoint.routeConstants.search})
                            .param("text", text)
                            .exchange();
    }

    /**
     * Submits a Delete request
     */
    protected MvcTestResult deleteEntity(String resourceId) {
        return mockMvcTester.delete().uri(${endpoint.entityName}Routes.${endpoint.routeConstants.delete}, resourceId).exchange();
    }

    /**
     * To support the use case of a well-formed update request
     */
    protected MvcTestResult updateEntity(${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvcTester.put().uri(${endpoint.entityName}Routes.${endpoint.routeConstants.update}, ${endpoint.entityVarName}.getResourceId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(${endpoint.entityVarName}))
                            .exchange();
    }

    /**
     * To support the use case of the ID in the query string not matching the ID in the payload
     */
    protected MvcTestResult updateEntity(String resourceId, ${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvcTester.put().uri(${endpoint.entityName}Routes.${endpoint.routeConstants.update}, resourceId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(${endpoint.entityVarName}))
                            .exchange();
    }

    /**
     * Submits a Post request
     */
    protected MvcTestResult createEntity(${endpoint.pojoName} ${endpoint.entityVarName}) throws Exception {
        return mockMvcTester.post().uri(${endpoint.entityName}Routes.${endpoint.routeConstants.create})
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(${endpoint.entityVarName}))
                            .exchange();

    }
}
