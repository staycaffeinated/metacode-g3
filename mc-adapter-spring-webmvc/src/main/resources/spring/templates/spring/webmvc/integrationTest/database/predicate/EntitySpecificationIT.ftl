package ${EntitySpecification.packageName()};

import ${RegisterDatabaseProperties.fqcn()};
import ${WebMvcEjbTestFixtures.fqcn()};
import ${EntitySpecification.fqcn()};
import ${Repository.fqcn()};
import ${Entity.fqcn()};
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ${EntitySpecification.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {

    @Autowired
    ${Repository.className()} repository;

    @BeforeEach
    public void setUp() {
        repository.saveAll(${EjbTestFixtures.className()}.allItems());
    }

    public void tearDown() {
        repository.deleteAll();
    }

    @Nested
    class ResourceIdUseCases {
        @Test
        void shouldFindByGivenId() {
            ${Entity.className()} entity = pickOne();
            String expectedResourceId = entity.getResourceId();

            Specification<${Entity.className()}> spec = ${EntitySpecification.className()}.builder()
                .resourceId(expectedResourceId)
                .build();

            List<${Entity.className()}> resultSet = repository.findAll(spec);

            // Style 1:
            resultSet.forEach(actual -> {
                assertThat(actual).returns(entity.getResourceId(), from(${Entity.className()}::getResourceId));
            });

            // Style 2:
            assertThat(resultSet)
                .isNotEmpty()
                .extracting(${Entity.className()}::getResourceId)
                .allMatch(actualResourceId -> actualResourceId.equals(entity.getResourceId()));
        }

        /*
         * The Specification builder only assembles non-null fields into the final
         * Specification instance it builds. If a field is `null`, the builder presumes
         * you don't care what the value is. With that semantic applied to `null`,
         * the Specification assembled in this test "correctly" selects all records.
         *
         * This test case is here to make this edge-case behavior known.
         */
        @Test
        void shouldReturnAllValuesWhenResourceIdIsNull() {
            Specification<${Entity.className()}> spec = ${EntitySpecification.className()}.builder().resourceId(null).build();

            List<${Entity.className()}> resultSet = repository.findAll(spec);

            // If null values are used in the builder, the field assigned null is actually ignored.
            // As such, this particular Specification ends up selecting _all_ records.
            // If you want to support searches of `null` values, feel free to tweak the
            // implementation of the ${EntitySpecification.className()} class.
            assertThat(resultSet).isNotEmpty();
        }
    }

    @Nested
    class TextValueUseCases {
        @Test
        void shouldFindByGivenValue() {
            ${Entity.className()} entity = pickOne();
            String expectedValue = entity.getText();

            Specification<${Entity.className()}> spec = ${EntitySpecification.className()}.builder()
                .text(expectedValue)
                .build();

            List<${Entity.className()}> resultSet = repository.findAll(spec);

            // Style 1:
            resultSet.forEach(actual -> {
                assertThat(actual).returns(expectedValue, from(${Entity.className()}::getText));
            });

            // Style 2:
            assertThat(resultSet)
                .isNotEmpty()
                .extracting(${Entity.className()}::getText)
                .allMatch(actualText -> actualText.equals(entity.getText()));
        }

        @Test
        void shouldFindAnyNullValues() {
            Specification<${Entity.className()}> spec = ${EntitySpecification.className()}.builder()
                .textIsNull(true)
                .build();

            List<${Entity.className()}> resultSet = repository.findAll(spec);

            // The test data does not add any null values, so this result will be empty.
            assertThat(resultSet).isEmpty();
        }

        @Test
        void shouldFindLikeValue() {
            ${Entity.className()} entity = pickOne();
            // Select a random string from one of the columns so we can exercise the LIKE condition
            String expectedText = entity.getText().substring(0,3).trim();

            Specification<${Entity.className()}> spec =${EntitySpecification.className()}.builder()
                .textIsLike('%' + expectedText + '%')
                .build();

            List<${Entity.className()}> resultSet = repository.findAll(spec);

            assertThat(resultSet)
                .isNotEmpty()
                .extracting(${Entity.className()}::getText)
                .allMatch(actualText -> actualText.contains(expectedText));
        }
    }

    /*
     * Pick an existing record from the database
     */
    ${Entity.className()} pickOne() {
        return repository.findAll(PageRequest.ofSize(10)).getContent().get(0);
    }

}