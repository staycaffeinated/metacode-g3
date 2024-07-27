<#include "/common/Copyright.ftl">
package ${AbstractIntegrationTest.packageName()};

import org.springframework.test.context.ActiveProfiles;

import static ${SpringProfiles.fqcn()}.INTEGRATION_TEST;

<#if (project.testcontainers)??>
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
<#if (project.testcontainers)?? &&  (project.postgres)??>
import org.testcontainers.containers.PostgreSQLContainer;
</#if>

@ActiveProfiles({INTEGRATION_TEST})
<#if (project.testcontainers)??>
@Testcontainers
</#if>
public abstract class ${AbstractIntegrationTest.className()} {
}