<#include "/common/Copyright.ftl">
package ${AbstractPostgresIntegrationTest.packageName()};

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import javax.script.ScriptException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractPostgresIntegrationTest.Initializer.class)
@SuppressWarnings({
    "java:S2187" // this isn't a Test class so it won't contain tests
})
@Slf4j
public class AbstractPostgresIntegrationTest {

    // Where Metacode writes db scripts when neither Liquibase nor Flyway are used
    // (although Flyway is not supported yet).
    private static final String SCHEMA_FOLDER = "db/scripts/";

    public static PostgreSQLContainer<?> postgreSQLContainer() {
        return Initializer.postgres;
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17").withReuse(true);

        private static void startContainers() {
            /*
             * Other containers like RabbitMQ or other databases and such
             * can also be added to this deepStart call.
             */
            Startables.deepStart(Stream.of(postgres)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                "spring.datasource.url", postgres.getJdbcUrl(),
                "spring.datasource.username", postgres.getUsername(),
                "spring.datasource.password", postgres.getPassword(),
                "spring.jpa.show-sql", "true",
                "spring.jpa.properties.hibernate.format_sql", "true");
        }

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
            startContainers();
            try {
                initTestContainerDatabase();
            }
            catch (IOException e) {
                log.error("Unable to initialize the test container database. {}", e.getLocalizedMessage(), e);
            }

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            MapPropertySource testcontainers = new MapPropertySource("testcontainers", createConnectionConfiguration());

            environment.getPropertySources().addFirst(testcontainers);
        }

    }

    /*
     * Initialize the database within the TestContainer database.
     * When using tools like Liquibase or Flyway, creating the database artifacts is done for you.
     * Without those tools, you usually have to depend on Hibernate or manually set up the database yourself.
     * When a database schema is _not_ used, Hibernate may be sufficient.
     * However, if you are using a database schema, Hibernate will not create the schema for you,
     * nor the artifacts within the schema (such as tables, indexes, sequences). Those artifacts
     * have to be created by you. This is where this method helps.
     *
     * See: https://stackoverflow.com/questions/53078306/populate-a-database-with-testcontainers-in-a-springboot-integration-test
     */
    public static void initTestContainerDatabase() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        final String pattern = "classpath:" + SCHEMA_FOLDER + "*.sql";
        Resource[] scripts = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);

        var containerDelegate = new JdbcDatabaseDelegate(postgreSQLContainer(), "");
        Arrays.stream(scripts).forEach(script -> {
            try {
                ScriptUtils.executeDatabaseScript(containerDelegate,
                                script.getFile().getParentFile().getAbsolutePath(),
                                loadScript(script));
            }
            catch (ScriptException | IOException e) {
                log.error("An error occurred while either loading or executing this database script: {}", SCHEMA_FOLDER + script.getFilename(), e);
            }
        });
    }

    private static String loadScript(Resource script) throws IOException {
        String fqnOfScript = script.getFile().getAbsolutePath();
        return FileUtils.readFileToString(new File(fqnOfScript), StandardCharsets.UTF_8);
    }

}
