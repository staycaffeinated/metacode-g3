<#include "/common/Copyright.ftl">
package ${project.basePackage}.database;

import ${project.basePackage}.config.ContainerConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface RegisterDatabaseProperties {

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", ContainerConfiguration::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "testdata"); // this property is not required
    }
}