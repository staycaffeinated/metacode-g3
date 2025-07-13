<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

<#if (endpoint.isWithTestContainers())>
import ${ContainerConfiguration.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${Document.fqcn()};
import ${ConcreteDocumentStoreApi.fqcn()};
import ${EntityResource.fqcn()};
import ${ModelTestFixtures.fqcn()};
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
<#if (endpoint.isWithTestContainers())>
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
<#if (endpoint.isWithTestContainers())>
@Import(ContainerConfiguration.class)
@Testcontainers
</#if>
class ${ServiceImpl.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
    @Autowired
    private ${ConcreteDocumentStoreApi.className()} dataStore;

    private ${ServiceApi.className()} serviceUnderTest;

    private ${endpoint.entityName} knownPersistedItem;

    @BeforeEach
    void init${endpoint.entityName}Service() {
        serviceUnderTest = new ${ServiceImpl.className()}(dataStore);
        ${ModelTestFixtures.className()}.allItems().forEach(dataStore::create);
        knownPersistedItem = dataStore.findAll().get(0);
    }

    @AfterEach
    void deleteTestData() {
        ${ModelTestFixtures.className()}.allItems().forEach(item ->
            dataStore.deleteByResourceId(item.getResourceId()));
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
            String expectedId = knownPersistedItem.getResourceId();

            // when: the service is asked to find the item
            Optional<${endpoint.entityName}> optional = serviceUnderTest.find${endpoint.entityName}ByResourceId(expectedId);

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
        void shouldCreateNew${endpoint.entityName}() {
            // given: a new item to be inserted into the database
            ${endpoint.pojoName} expected = ${ModelTestFixtures.className()}.oneWithoutResourceId();

            // when: the service is asked to create the item
            ${endpoint.pojoName} actual = serviceUnderTest.create${endpoint.entityName}(expected);

            // expect: the item is added, and its returned, along with the ID newly assigned
            // to it
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
            ${endpoint.entityName} modified = knownPersistedItem;
            final String newValue = "modified";
            modified.setText(newValue);

            List<${endpoint.entityName}> modifiedList = serviceUnderTest.update${endpoint.entityName}(modified);

            assertThat(modifiedList).isNotNull();
            assertThat(modifiedList.size()).isGreaterThan(0);
            modifiedList.forEach(pojo -> {
            assertThat(pojo.getResourceId()).isEqualTo(knownPersistedItem.getResourceId());
            assertThat(pojo.getText()).isEqualTo(newValue);
            });
        }
    }

    /*
    * Delete method
    */
    @Nested
    class Delete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() {
            // given: the ID of an item known to exist in the database
            String knownId = ${ModelTestFixtures.className()}.sampleTwo().getResourceId();

            // given: the service is asked to delete the item
            serviceUnderTest.delete${endpoint.entityName}ByResourceId(knownId);

            // expect: a subsequent attempt to find the deleted item comes back empty
            Optional<${endpoint.entityName}> option = serviceUnderTest.find${endpoint.entityName}ByResourceId(knownId);
            assertThat(option).isNotNull().isNotPresent();
        }
    }

    @Nested
    class FindByText {
        @Test
        void shouldFindResults() {
            // scenario: search for items having a property value known to exist
            Page<${endpoint.entityName}> rs = serviceUnderTest.findByText(knownPersistedItem.getText(), Pageable.ofSize(5));
            
            assertThat(rs).isNotEmpty();
        }
    }
}
