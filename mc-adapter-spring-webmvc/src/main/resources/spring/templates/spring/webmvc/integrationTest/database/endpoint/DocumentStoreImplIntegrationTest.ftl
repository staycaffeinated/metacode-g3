package ${DocumentKindStoreProvider.packageName()};

import ${EntityResource.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import ${ContainerConfiguration.fqcn()};
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(${ContainerConfiguration.className()}.class)
@Testcontainers
@SuppressWarnings("unchecked")
class ${DocumentKindStoreProvider.integrationTestClass()} {

    @Autowired
    ${DocumentKindStoreProvider.className()} documentStoreUnderTest;

    @Autowired
    ${Repository.className()} repository;

    @BeforeEach
    public void setUp() throws Exception {
        repository.insert(${DocumentTestFixtures.className()}.allItems());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Nested
    class QueryUseCases {
        @Test
        void shouldFindByResourceId() {
            // Given: an ID known to exist in the database
            String resourceId = pickOne().getResourceId();

            // When: searching for that ID
            Optional<${EntityResource.className()}> maybe = documentStoreUnderTest.findByResourceId(resourceId);

            // Expect: A document having that ID is found
            assertThat(maybe).isPresent();
            assertThat(maybe.get().getResourceId()).isEqualTo(resourceId);
        }

        @Test
        void shouldReturnEmptyWhenIdNotFound() {
            // Given: an ID that does not exist in the database
            Optional<${EntityResource.className()}> maybe = documentStoreUnderTest.findByResourceId("abcdefg12345");

            // Expect: an empty result is returned
            assertThat(maybe).isEmpty();
        }

        @ParameterizedTest
        @NullSource
        void shouldThrowExceptionWhenRequiredIdIsNull(String id) {
            assertThrows(NullPointerException.class, () -> documentStoreUnderTest.findByResourceId(id));
        }

        @Test
        void shouldFindAll() {
            List<${EntityResource.className()}> rs = documentStoreUnderTest.findAll();
            assertThat(rs).isNotEmpty();
        }

        @Test
        void shouldSearchForOne() {
            String searchText = pickOne().getText();
            Pageable page = PageRequest.of(0, 10);
            Page<${EntityResource.className()}> pageResult = documentStoreUnderTest.findByText(searchText, page);

            assertThat(pageResult).isNotEmpty();
        }
    }

    @Nested
    class CommandUseCases {
        @Test
        void shouldCreateOne() {
            ${EntityResource.className()} sample = ${WebMvcModelTestFixtures.className()}.sampleOne();

            ${EntityResource.className()} persistedItem = documentStoreUnderTest.create(sample);
            assertThat(persistedItem).isNotNull();
        }

        @Test
        void shouldUpdateOne() {
            // Given: Some document that already exists in the database
            ${EntityResource.className()} sample = ${WebMvcModelTestFixtures.className()}.sampleTwo();
            ${EntityResource.className()} persistedItem = documentStoreUnderTest.create(sample);
            assertThat(persistedItem).isNotNull();

            // When: updating some field of an existing document
            final String newText = "This is some random text value";
            persistedItem.setText(newText);
            List<${EntityResource.className()}> resultSet = documentStoreUnderTest.update(persistedItem);

            // Expect: the update was applied to at least one document
            assertThat(resultSet).isNotEmpty();

            // Expect: searching for documents with the updated value returns at least one document
            Pageable page = PageRequest.of(0, 10);
            Page<${EntityResource.className()}> pageResult = documentStoreUnderTest.findByText(newText, page);
            assertThat(pageResult).isNotEmpty();
        }

        @Test
        void shouldDeleteOne() {
            String resourceId = pickOne().getResourceId();
            long numTouched = documentStoreUnderTest.deleteByResourceId(resourceId);
            assertThat(numTouched).isPositive();
        }
    }

    /* ------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------ */
    ${Document.className()} pickOne() {
        return repository.findAll().get(0);
    }
}

