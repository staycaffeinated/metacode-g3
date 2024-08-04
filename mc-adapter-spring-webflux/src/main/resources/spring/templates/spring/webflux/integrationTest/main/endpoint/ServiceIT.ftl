<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import ${PostgresDbContainerTests.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import ${ObjectDataStore.fqcn()};
import ${EjbTestFixtures.fqcn()};
import ${EntityResource.fqcn()};
import ${Entity.fqcn()};
import ${ModelTestFixtures.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${ContainerConfiguration.fqcn()};
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Integration test of the service component
 */
@ComponentScan(basePackageClasses={
    ${ContainerConfiguration.className()}.class})
@SpringBootTest
@Slf4j
<#if (endpoint.isWithPostgres() && endpoint.isWithTestContainers())>
class ${ServiceImpl.integrationTestClass()} extends ${PostgresDbContainerTests.className()} {
<#else>
class ${ServiceImpl.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
</#if>

    @Autowired
    ${ObjectDataStore.className()} dataStore;

    ${ServiceImpl.className()} serviceUnderTest;

    @BeforeEach
    void setUp() {
        serviceUnderTest = new ${ServiceImpl.className()}(dataStore);
        ${ModelTestFixtures.className()}.allItems().forEach(item -> {
          serviceUnderTest.create${endpoint.entityName}(item).blockOptional(Duration.ofSeconds(1)); 
        });
    }

    @AfterEach
    void tearDown() {
        // empty
    }

    @Test
    void shouldFindResults() {
        int expectedCount = ${EjbTestFixtures.className()}.allItems().size();

        Flux<${endpoint.entityName}> source = serviceUnderTest.findAll${endpoint.entityName}s();

        // Expect as many rows back as were inserted by setUp 
        StepVerifier.create(source).expectSubscription().expectNextCount(expectedCount).thenCancel().verify();
    }
}
