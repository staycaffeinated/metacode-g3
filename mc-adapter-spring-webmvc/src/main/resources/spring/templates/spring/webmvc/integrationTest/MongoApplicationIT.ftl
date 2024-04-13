<#include "/common/Copyright.ftl">
package ${project.basePackage};

<#if (project.isWithTestContainers())>
    import ${project.basePackage}.config.ContainerConfiguration;
</#if>
import ${project.basePackage}.database.RegisterDatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
<#if (project.isWithTestContainers())>
    import org.springframework.context.annotation.Import;
    import org.testcontainers.junit.jupiter.Testcontainers;
</#if>
@Slf4j
@SpringBootTest
<#if (project.isWithTestContainers())>
    @Import(ContainerConfiguration.class)
    @Testcontainers
</#if>
class ApplicationTests implements RegisterDatabaseProperties {

@Test
@SuppressWarnings("java:S2699") // there's nothing to assert
void contextLoads() {
// If this test runs without throwing an exception, then SpringBoot started successfully
}
}
