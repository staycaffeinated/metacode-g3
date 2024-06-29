<#include "/common/Copyright.ftl">

package ${Repository.packageName()};

<#if endpoint.isWithTestContainers()>
import ${ContainerConfiguration.fqcn()};
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
import ${Entity.fqcn()};
import ${EntityWithText.fqcn()};
import ${WebMvcEjbTestFixtures.fqcn()};

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import org.springframework.boot.test.context.SpringBootTest;
</#if>
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
</#if>

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
</#if>
/**
* These tests verify custom queries added to the repository.
* If the JPA queries are not modified, or not custom methods are added to the
* Repository class, these tests may be deleted.
*/
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ContainerConfiguration.class)
@Testcontainers
class ${Repository.integrationTestClass()} implements RegisterDatabaseProperties {
<#else>
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ${Repository.integrationTestClass()} implements RegisterDatabaseProperties {
</#if>
    @Autowired
    private ${Repository.className()} repositoryUnderTest;

    // Increment for rowIds in the database
    private long rowId = 0;

    @BeforeEach
    void insertTestData() {
        repositoryUnderTest.saveAll(${WebMvcEjbTestFixtures.className()}.allItems());
    }

    @AfterEach
    public void tearDownEachTime() {
        repositoryUnderTest.deleteAll();
        repositoryUnderTest.flush();
    }

    @SuppressWarnings({"java:S125"}) // false positive: this comment block does not contain code
    /*
     * Test custom methods.
     *
     * Its worth testing custom queries to ensure the query semantics are correct;
     * simply having proper syntax does not ensure the records that _should_ be
     * returned are the one's being returned.
     *
     * The scope of these tests is to verify the semantics of custom JPA queries added
     * to the Repository interface. The repository methods that are available out-of-the-box,
     * such as findById, do not need to be tested. It's entirely possible that this test class
     * can be removed altogether.
     */
    @Nested
    class ValidateCustomMethod {
        /**
         * This is an example test. You do not actually need to verify the findAll method.
         * This test is only an example of how you might want to write such a test.
         */
        @Test
        void testFindAll() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<${Entity.className()}> page = repositoryUnderTest.findAll(pageable);

            assertThat(page).isNotNull();
            assertThat(page.hasContent()).isTrue();
        }
    }

    @Nested
    class ValidatePredicates {
        @Test
        void shouldIgnoreCase() {
            String text = ${WebMvcEjbTestFixtures.className()}.allItems().get(0).getText();
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}(text);
            List<${Entity.className()}> list = repositoryUnderTest.findAll(spec);
            assertThat(list).isNotNull().hasSize(1);
        }

        @Test
        void shouldFindAllWhenValueIsEmpty() {
            ${EntityWithText.className()} spec = new ${EntityWithText.className()}("");
            List<${Entity.className()}> list = repositoryUnderTest.findAll(spec);
            long count = repositoryUnderTest.count();
            assertThat(list).isNotNull().hasSize((int)count);
        }
    }
}
