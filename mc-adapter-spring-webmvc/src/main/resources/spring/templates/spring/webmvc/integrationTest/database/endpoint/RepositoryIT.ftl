<#include "/common/Copyright.ftl">

package ${endpoint.basePackage}.database.${endpoint.lowerCaseEntityName};

<#if endpoint.isWithTestContainers()>
    import ${endpoint.basePackage}.config.ContainerConfiguration;
    import org.springframework.context.annotation.Import;
    import org.testcontainers.junit.jupiter.Testcontainers;
    import ${endpoint.basePackage}.config.ContainerConfiguration;
</#if>
import ${endpoint.basePackage}.database.*;
import ${endpoint.basePackage}.database.${endpoint.lowerCaseEntityName}.*;
import ${endpoint.basePackage}.database.${endpoint.lowerCaseEntityName}.predicate.*;
import ${endpoint.basePackage}.math.SecureRandomSeries;
import ${endpoint.basePackage}.spi.ResourceIdSupplier;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

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
    class ${endpoint.entityName}RepositoryIT implements RegisterDatabaseProperties {
<#else>
    @DataJpaTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    class ${endpoint.entityName}RepositoryIT implements RegisterDatabaseProperties {
</#if>
@Autowired
private ${endpoint.entityName}Repository repositoryUnderTest;

// Generates the public identifier of an entity
private final ResourceIdSupplier randomSeries = new SecureRandomSeries();

// Increment for rowIds in the database
private long rowId = 0;

@BeforeEach
void insertTestData() {
repositoryUnderTest.saveAll(${endpoint.ejbName}TestFixtures.allItems());
}

@AfterEach
public void tearDownEachTime() {
repositoryUnderTest.deleteAll();
}

/*
* Test custom methods.
*
* Its worth testing custom queries to ensure the query semantics are correct;
* simply having proper syntax does not ensure the records that _should_ be
* returned are the one's being returned.
*
* The scope of these tests is verify the semantics of custom JPA queries added
* to the Repository interface. The repository methods that are available out-of-the-box,
* such as findById, do not need to be tested. Its entirely possible that this test class
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
Page<${endpoint.ejbName}> page = repositoryUnderTest.findAll(pageable);

assertThat(page).isNotNull();
assertThat(page.hasContent()).isTrue();
}
}

@Nested
class ValidatePredicates {
@Test
void shouldIgnoreCase() {
String text = ${endpoint.ejbName}TestFixtures.allItems().get(0).getText();
${endpoint.entityName}WithText spec = new ${endpoint.entityName}WithText(text);
List<${endpoint.ejbName}> list = repositoryUnderTest.findAll(spec);
assertThat(list).isNotNull().hasSize(1);
}

@Test
void shouldFindAllWhenValueIsEmpty() {
${endpoint.entityName}WithText spec = new ${endpoint.entityName}WithText("");
List<${endpoint.ejbName}> list = repositoryUnderTest.findAll(spec);
assertThat(list).isNotNull().hasSameSizeAs(${endpoint.ejbName}TestFixtures.allItems());
}
}

// ------------------------------------------------------------------------------------------------------------
//
// Helper methods
//
// ------------------------------------------------------------------------------------------------------------

private ${endpoint.ejbName} new${endpoint.ejbName}(final String value)  {
return new ${endpoint.ejbName}(++rowId, randomSeries.nextResourceId(), value);
}

}
