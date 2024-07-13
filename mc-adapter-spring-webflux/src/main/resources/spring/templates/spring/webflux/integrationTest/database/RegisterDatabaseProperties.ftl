<#include "/common/Copyright.ftl">
package ${project.basePackage}.database;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface RegisterDatabaseProperties {

    @DynamicPropertySource
    public static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("spring.datasource.initialization-mode", () -> "embedded");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
        registry.add("spring.jpa.properties.hibernate.id.new_generator_mappings", () -> "false");

        // The username and password here must match the username and password in the
        // src/test/resources/application-test.yaml.  Otherwise, you will see
        // "org.h2.jdbc.JdbcSQLInvalidAuthorizationSpecException: Wrong user name or password"
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "password");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }
}