<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

<#if endpoint.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import ${EntityResource.fqcn()};
import ${Entity.fqcn()};
import ${Repository.fqcn()};
import ${ObjectDataStore.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import ${WebMvcEjbTestFixtures.fqcn()};
import ${RegisterDatabaseProperties.fqcn()};
import org.junit.jupiter.api.*;
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import org.springframework.context.annotation.Import;
<#else>
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
</#if>
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
<#if endpoint.isWithTestContainers()>
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
</#if>
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
<#if endpoint.isWithTestContainers()>
@Import(ContainerConfiguration.class)
@Testcontainers
<#else>
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
</#if>
class ${ServiceImpl.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    private ${Repository.className()} ${Repository.varName()};

    @Autowired
    private ${ObjectDataStore.className()} ${endpoint.entityVarName}DataStore;

    private ${ServiceImpl.className()} serviceUnderTest;

    @BeforeEach
    void init${endpoint.entityName}Service() {
        serviceUnderTest = new ${ServiceImpl.className()}(${endpoint.entityVarName}DataStore);
    }

    @BeforeEach
    void insertTestData() {
        ${Repository.varName()}.saveAll(${WebMvcEjbTestFixtures.className()}.allItems());
    }

    @AfterEach
    void deleteTestData() {
        ${Repository.varName()}.deleteAll();
    }

    /*
     * FindById
     */
    @Nested
    class FindByResourceId {
        @Test
        @SuppressWarnings("all")
        void shouldFind${endpoint.entityName}ById() throws Exception {
            // given: the public ID of an item known to be in the database
            String expectedId = pickOne().getResourceId();

            // when: the service is asked to find the item
            Optional<${endpoint.pojoName}> optional = serviceUnderTest.find${endpoint.entityName}ByResourceId(expectedId);

            // expect: the item is found, and has the ID that's expected
            assertThat(optional).isNotNull().isPresent();
            assertThat(optional.get().getResourceId()).isEqualTo(expectedId);
        }
    }

    /*
    * Create method
    */
    @Nested
    class Create${endpoint.entityName} {
        @Test
        void shouldCreateNew${endpoint.entityName}() throws Exception {
            // given: a new item to be inserted into the database
            ${endpoint.pojoName} expected = ${WebMvcModelTestFixtures.className()}.oneWithoutResourceId();

            // when: the service is asked to create the item
            ${endpoint.pojoName} actual = serviceUnderTest.create${endpoint.entityName}(expected);

            // expect: the item is added, and its returned, along with the ID newly assigned to it
            assertThat(actual).isNotNull().hasNoNullFieldsOrProperties();
            assertThat(actual.getResourceId()).isNotBlank().isNotEmpty();
        }
    }


    /*
     * Update method
     */
    @Nested
    class Update${endpoint.entityName} {

        @Test
        @SuppressWarnings("all")
        void shouldUpdate${endpoint.entityName}() throws Exception {
            String resourceId = pickOne().getResourceId();
            ${endpoint.pojoName} modified = ${WebMvcModelTestFixtures.className()}.oneWithoutResourceId();
            final String newValue = "modified";
            modified.setText(newValue);
            modified.setResourceId(resourceId); // use and ID known to exit

            Optional<${endpoint.pojoName}> option = serviceUnderTest.update${endpoint.entityName}(modified);

            assertThat(option).isNotNull().isPresent();
            assertThat(option.get().getResourceId()).isEqualTo(resourceId);
            assertThat(option.get().getText()).isEqualTo(newValue);
        }
    }

    /*
    * Delete method
    */
    @Nested
    class Delete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            // given: the ID of an item known to exist in the database
            String knownId = ${WebMvcEjbTestFixtures.className()}.allItems().get(2).getResourceId();

            // given: the service is asked to delete the item
            serviceUnderTest.delete${endpoint.entityName}ByResourceId(knownId);

            // expect: a subsequent attempt to find the deleted item comes back empty
            Optional<${endpoint.pojoName}> option = serviceUnderTest.find${endpoint.entityName}ByResourceId(knownId);
            assertThat(option).isNotNull().isNotPresent();
        }
    }

    /**
     * Returns one of the records persisted in the test database
     */
    ${Entity.className()} pickOne() {
        ${Entity.className()} sample = ${WebMvcEjbTestFixtures.className()}.sampleOne();
        ${Entity.className()} probe = ${Entity.className()}.builder().text(sample.getText()).build();
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreNullValues();
        Example<${Entity.className()}> example = Example.of(probe, matcher);
        List<${Entity.className()}> possible = ${Repository.varName()}.findAll(example);
        assertThat(possible).isNotEmpty();
        return possible.get(0);
    }
}
