<#include "/common/Copyright.ftl">

package ${MongoDataStoreProvider.packageName()};

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ${BadRequestException.fqcn()};
import ${DocumentToPojoConverter.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${Repository.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ModelTestFixtures.fqcn()};
import ${DocumentTestFixtures.fqcn()};

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit tests of the DocumentStoreImpl. Most of these tests fall into the "white box testing"
 * category, since the tests emulate the different behaviors that come about in the underlying
 * repository, particularly MongoTemplate behavior in various scenarios.
 */
@ExtendWith(SpringExtension.class)
@SuppressWarnings("unchecked")
class ${MongoDataStoreProvider.testClass()} {

    private final ${DocumentToPojoConverter.className()} documentConverter = new ${DocumentToPojoConverter.className()}();
    private final ${PojoToDocumentConverter.className()} pojoConverter = new ${PojoToDocumentConverter.className()}();
    private final ${ResourceIdSupplier.className()} resourceIdGenerator = new ${SecureRandomSeries.className()}();

    @Mock
    private ${Repository.className()} mockRepository;

    @Mock
    private MongoTemplate mockMongoTemplate;

    @InjectMocks
    ${MongoDataStoreProvider.className()} documentStore;

    @BeforeEach
    public void setUp() {
        documentStore = ${MongoDataStoreProvider.className()}.builder()
            .documentConverter(documentConverter)
            .pojoConverter(pojoConverter)
            .resourceIdGenerator(resourceIdGenerator)
            .repository(mockRepository)
            .mongoTemplate(mockMongoTemplate)
            .build();
    }

    @Nested
    class FindAllUseCases {
        @Test
        void shouldFindAll() {
            // given: repo.findAll returns a non-empty list of documents
            List<Object> list = new ArrayList<>(${DocumentTestFixtures.className()}.allItems());
            when(mockMongoTemplate.findAll(any(), anyString())).thenReturn(list);

            // when: documentStore.findAll
            List<${EntityResource.className()}> resultSet = documentStore.findAll();

            // expect: the documents are returned converted to POJOs
            assertThat(resultSet).isNotEmpty();
        }
    }

    @Nested
    class AddUseCases {
        @Test
        void shouldCreateOne() {
            // given: repo.save will return the document that was persisted
            when(mockMongoTemplate.save(any(${Document.className()}.class), anyString()))
                .thenReturn(${DocumentTestFixtures.className()}.sampleOne());

            // when: documentStore.create is called to save a POJO
            ${EntityResource.className()} savedOne = documentStore.create(${ModelTestFixtures.className()}.oneWithoutResourceId());

            // expect: the POJO is returned.
            assertThat(savedOne).isNotNull();
        }
    }

    @Nested
    class FindByResourceIdUseCases {
        @Test
        void shouldFindByResourceId() {
            // given: repo.findOne [having the given id] successfully finds a document
            when(mockMongoTemplate.findOne(any(Query.class), any(Class.class), anyString()))
                .thenReturn(${DocumentTestFixtures.className()}.sampleOne());

            // when: documentStore.findBy
            Optional<${EntityResource.className()}> maybe = documentStore.findByResourceId("123");

            // expect: a POJO is returned (wrapped in an Optional)
            assertThat(maybe).isPresent();
       }

        @Test
        void shouldReturnEmptyIfNotFound() {
           // given: repo.findOne [having a given id] does not find any matching documents
           when(mockMongoTemplate.findOne(any(Query.class), any(Class.class), anyString()))
               .thenReturn(null);

           // when: documentStore.findBy
           Optional<Character> maybe = documentStore.findByResourceId("123");

           // expect: an empty Optional is returned, since no matches were found
           assertThat(maybe).isEmpty();
        }

        @ParameterizedTest
        @NullSource
        void shouldThrowExceptionIfIdIsNull(String resourceId) {
            assertThrows(NullPointerException.class, () -> documentStore.findByResourceId(resourceId));
        }
    }

    @Nested
    class FindByTextUseCases {
        @Test
        void shouldFindByText() {
            // given: repo.findBy returns a page of matching documents
            Page<${Document.className()}> mockPage = Mockito.mock(Page.class);
            when(mockPage.hasContent()).thenReturn(true);
            when(mockPage.isEmpty()).thenReturn(false);
            when(mockRepository.findByTextContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(mockPage);

            // when: dataStore.findBy
            Page<${EntityResource.className()}> resultSet = documentStore.findByText("something", Pageable.unpaged());

            // expect: a non-empty page of results is returned
            assertThat(resultSet).isNotNull();
        }

        @ParameterizedTest
        @NullSource
        void shouldThrowExceptionIfSearchTextIsNull(String searchText) {
            Pageable page = Pageable.unpaged();
            assertThrows(NullPointerException.class, () -> documentStore.findByText(searchText, page));
        }
    }

    @Nested
    class DeleteUseCases {
        @ParameterizedTest
        @NullSource
        void shouldThrowExceptionIfIdIsNull(String id) {
            assertThrows(NullPointerException.class, () -> documentStore.deleteByResourceId(id));
        }

        @Test
        void whenDocumentsAreDeleted_shouldReturnSomeCount() {
            DeleteResult mockResult = Mockito.mock(DeleteResult.class);
            when(mockResult.getDeletedCount()).thenReturn(1L);
            when(mockMongoTemplate.remove(any(Query.class), anyString())).thenReturn(mockResult);

            long count = documentStore.deleteByResourceId("123");
            assertThat(count).isPositive();
        }
    }


    @Nested
    class UpdateUseCases {
        @Test
        void whenNoMatchingRecordsFoundToUpdate_expectEmptyResultSet() {
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            when(mockMongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), anyString()))
                .thenReturn(mockUpdateResult);
            when(mockUpdateResult.getModifiedCount()).thenReturn(0L);

            List<${EntityResource.className()}> resultSet = documentStore.update(${ModelTestFixtures.className()}.sampleOne());

            assertThat(resultSet).isEmpty();
        }

        /*
         * This is an edge case. If Mongo's 'updateMulti' method finds matching records,
         * but 'find' does not find matching records, then expect an empty result set is returned.
         */
        @Test
        void whenFindModifiedItemReturnsEmptyHanded_expectEmptyResultSet() {
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            when(mockMongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), anyString()))
                .thenReturn(mockUpdateResult);
            when(mockUpdateResult.getModifiedCount()).thenReturn(1L);
            when(mockMongoTemplate.find(any(Query.class), any(Class.class), anyString()))
                .thenReturn(List.of());

            List<${EntityResource.className()}> resultSet = documentStore.update(${ModelTestFixtures.className()}.sampleOne());

            assertThat(resultSet).isEmpty();
        }

        @Test
        void whenUpdateTouchesSomeRecord_expectNonEmptyResultSet() {
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            when(mockMongoTemplate.updateMulti(any(Query.class), any(UpdateDefinition.class), anyString()))
                .thenReturn(mockUpdateResult);
            when(mockUpdateResult.getModifiedCount()).thenReturn(1L);
            when(mockMongoTemplate.find(any(Query.class), any(Class.class), anyString()))
                .thenReturn(List.of(${DocumentTestFixtures.className()}.sampleOne()));

            List<${EntityResource.className()}> resultSet = documentStore.update(${ModelTestFixtures.className()}.sampleOne());

            assertThat(resultSet).isNotEmpty();
        }
    }

}
