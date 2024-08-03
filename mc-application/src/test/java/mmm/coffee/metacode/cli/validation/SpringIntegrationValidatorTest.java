package mmm.coffee.metacode.cli.validation;


import mmm.coffee.metacode.spring.constant.SpringIntegrations;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SpringIntegrationValidator.
 * <p>
 * When creating a project, the end-user can indicate additional dependencies
 * with the {@code --add} option, such as:
 * <code>
 *     create project spring-webmvc -n petstore -p acme.petstore --add postgres testcontainers
 * </code>
 * Some of the additional dependencies are mutually exclusive; the code generator does not
 * support generating code for both dependencies. Case in point is PostgreSQL and MongoDB;
 * the code generator can either create Entities for PostgreSQL, or Documents for MongoDB,
 * but not both. There's also the challenge of having multiple DataSource's and knowing when
 * to use which DataSource.
 * </p>
 * </p>
 */
class SpringIntegrationValidatorTest {

    /**
     * The equivalent of, from the command line, --add postgres
     */
    private final SpringIntegrations[] onlyPostgres = {
            SpringIntegrations.POSTGRES
    };

    /**
     * The equivalent of, from the command line, --add mongodb
     */
    private final SpringIntegrations[] onlyMongoDb = {
            SpringIntegrations.MONGODB
    };

    /**
     * The equivalent of, from the command line, --add postgres mongodb
     */
    private final SpringIntegrations[] bothPostgresAndMongoDb = {
            SpringIntegrations.POSTGRES,
            SpringIntegrations.MONGODB
    };

    @Test
    void shouldAllowPostgresWithoutMongoDb() {
        assertThat(SpringIntegrationValidator.of(onlyPostgres).isValid()).isTrue();
    }

    @Test
    void shouldAllowMongoDbWithoutPostgres() {
        assertThat(SpringIntegrationValidator.of(onlyMongoDb).isValid()).isTrue();
    }

    @Test
    void shouldFlagPostgresAndMongoDbTogetherAsInvalid() {
        // Support for PostgreSQL and MongoDB at same time is not supported
        assertThat(SpringIntegrationValidator.of(bothPostgresAndMongoDb).isValid()).isFalse();
        // When an invalid combination is attempted, there should be an error message
        assertThat(SpringIntegrationValidator.of(bothPostgresAndMongoDb).errorMessage()).isNotEmpty();
    }

    @Nested
    class ErrorMessageTests {
        @Test
        void shouldReturnNonEmptyErrorWhenInvalidOptions() {
            var validator = SpringIntegrationValidator.of(bothPostgresAndMongoDb);
            assertThat(validator.isValid()).isFalse();
            assertThat(validator.errorMessage()).isNotEmpty();

        }

        @Test
        void shouldReturnEmptyErrorWhenValidOptions() {
            var validator = SpringIntegrationValidator.of(onlyPostgres);
            assertThat(validator.isValid()).isTrue();
            assertThat(validator.errorMessage()).isEmpty();
        }
    }
}
