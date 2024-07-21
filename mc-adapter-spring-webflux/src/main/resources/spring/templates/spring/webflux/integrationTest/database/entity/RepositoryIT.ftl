<#include "/common/Copyright.ftl">
package ${Repository.packageName()};


import ${ContainerConfiguration.fqcn()};
<#if endpoint.isWithPostgres() && endpoint.isWithTestContainers()>
import ${PostgresTestContainer.fqcn()};
</#if>
import ${RegisterDatabaseProperties.fqcn()};
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository integration help catch syntax errors in custom
 * queries that may get added to the Repository interface.
 * This class only needs to test those repository methods
 * that use custom queries; there's no value added by
 * testing the default repository query methods.
 */
@SpringBootTest
@ComponentScan(basePackageClasses = {${ContainerConfiguration.className()}.class})
<#if ((endpoint.isWithPostgres()) && (endpoint.isWithTestContainers()))>
class ${Repository.integrationTestClass()} extends ${PostgresTestContainer.className()} {
<#else>
class ${Repository.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {
</#if>

    @Autowired
    ${Repository.className()} repositoryUnderTest;

    @Test
    void shouldFindFirstRecord() {
        ${endpoint.ejbName} firstOne = repositoryUnderTest.findAll().blockFirst();
        assertThat(firstOne).isNotNull();
    }
}


