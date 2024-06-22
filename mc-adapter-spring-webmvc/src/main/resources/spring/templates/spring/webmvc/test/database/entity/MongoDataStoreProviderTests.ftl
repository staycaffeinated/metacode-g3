<#include "/common/Copyright.ftl">

package ${MongoDataStoreProvider.packageName()};

import ${DocumentToPojoConverter.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${EntityResource.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
* Unit test the ${endpoint.entityName}DataStore
*/
@ExtendWith(MockitoExtension.class)
@AutoConfigureDataMongo
@SuppressWarnings("all")
public class ${MongoDataStoreProvider.testName()} {

    ${DocumentToPojoConverter.className()} documentToPojoConverter = new ${DocumentToPojoConverter.className()}();
    ${PojoToDocumentConverter.className()} pojoToDocumentConverter = new ${PojoToDocumentConverter.className()}();
    ${ResourceIdSupplier.className()} randomSeries = new ${SecureRandomSeries.className()}();

    @Mock
    MongoTemplate mockMongoTemplate;

    @Mock
    ${Repository.className()} mockRepository;

    ${MongoDataStoreProvider.className()} dataStoreUnderTest;

    @BeforeEach
    void setUp() {
        // @formatter:off
        dataStoreUnderTest = ${endpoint.entityName}DataStoreProvider.builder()
            .documentConverter(documentToPojoConverter)
            .pojoConverter(pojoToDocumentConverter)
            .resourceIdGenerator(randomSeries)
            .mongoTemplate(mockMongoTemplate)
            .repository(mockRepository)
            .build();
        // @formatter:on
    }

    @Nested
    class FindByResourceId {
        @Test
        void shouldFindOne() {
            // scenario: an item is know to exist in the database
            ${Document.className()} expectedDocument = ${DocumentTestFixtures.className()}.oneWithResourceId();
            String expectedResourceId = ${DocumentTestFixtures.className()}.oneWithResourceId().getResourceId();
            when(mockMongoTemplate.findOne(any(), any(), any())).thenReturn(expectedDocument);

            // when: the dataStore is asked to find the known-to-exist item by its
            // publicId...
            Optional<${EntityResource.className()}> actualResult = dataStoreUnderTest.findByResourceId(expectedResourceId);

            assertThat(actualResult).isNotNull().isPresent();
            assertThat(actualResult.get().getResourceId()).isEqualTo(expectedResourceId);
        }

        @Test
        void shouldReturnEmptyWhenNoMatchingRecord() {
            // scenario: the repository cannot find any such record...
            ${Document.className()} expectedDocument = null;
            given(mockMongoTemplate.findOne(any(), any(), any())).willReturn(expectedDocument);

            // when: the dataStore is asked to find an unknown record...
            Optional<${EntityResource.className()}> actualResult = dataStoreUnderTest.findByResourceId("12345");

            // expect: an empty Optional is returned
            assertThat(actualResult).isNotNull().isEmpty();
        }

        @Test
        void shouldThrowExceptionWhenKeyIsNull() {
            assertThrows(NullPointerException.class, () -> dataStoreUnderTest.findByResourceId(null));
        }

        /*
        * This verifies the edge case that, when the converter attempts to transform an
        * entity into a POJO, a Null value is returned.
        */
        @Test
        void shouldReturnEmptyResultWhenConverterReturnsNull() {
            // Create a Mock documentToPojoConverter to enable testing a particular branch of code
            ${DocumentToPojoConverter.className()} mockDocumentToPojoConverter = Mockito.mock(${DocumentToPojoConverter.class()} );

            // Create a DataStore that uses the mock converter
            ${MongoDataStore.className()} storeUnderTest = new ${endpoint.entityName}DataStoreProvider(mockDocumentToPojoConverter, pojoToDocumentConverter,
            randomSeries, mockMongoTemplate, mockRepository);

            // given: the repository is able to find a particular entity
            // but when the entity is converted to a POJO, a NULL value is returned
            ${Document.className()} targetEntity = ${DocumentTestFixtures.className()}.oneWithResourceId();
            given(mockMongoTemplate.findOne(any(), any(), any())).willReturn(targetEntity);
            given(mockDocumentToPojoConverter.convert(any(${Document.className()}.class))).willReturn(null);

            // when:
            Optional<${EntityResource.className()}> actual = storeUnderTest.findByResourceId("12345");

            // expect
            assertThat(actual).isNotNull().isEmpty();
        }
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindMultipleItems() {
            // scenario: the repository contains multiple ${EntityResource.className()} entities
            List<${Document.className()}> resultSet = ${DocumentTestFixtures.className()}.allItems();
            given(mockMongoTemplate.findAll(any(), any(String.class))).willReturn(List.copyOf(resultSet));

            // when:
            List<${EntityResource.className()}> items = dataStoreUnderTest.findAll();

            // expect: the resultSet contains the same number of items as were found in the
            // repository
            assertThat(items).isNotNull().isNotEmpty().hasSize(${DocumentTestFixtures.className()}.allItems().size());
        }

        @Test
        void shouldHaveEmptyResults() {
            // scenario: the repository contains no ${endpoint.entityName} entities
            given(mockMongoTemplate.findAll(any(), any(String.class))).willReturn(List.of());

            // when:
            List<${EntityResource.className()}> items = dataStoreUnderTest.findAll();

            // expect: the resultSet is empty
            assertThat(items).isNotNull().isEmpty();
        }
    }

    @Nested
    class Save {
        /*
        * Context: when a new record is created in the database, a resourceId is
        * assigned to that record. The scope of this test is to verify that resourceId
        * is assigned and is part of the return value.
        */
        @Test
        void shouldAssignResourceIdToSavedItem() {
            // scenario: a ${endpoint.entityName} is successfully saved in the repository, and assigned a
            // resourceId.
            // Under the covers, the EJB is assigned a resourceId, then saved, so the record
            // returned by the repository should also have a resourceId. When the EJB is
            // transformed into a POJO, the POJO should retain the resourceId
            ${Document.className()} savedEntity = ${DocumentTestFixtures.className()}.oneWithResourceId();
            given(mockMongoTemplate.save(any(), any())).willReturn(savedEntity);
            ${EntityResource.className()} pojoToSave = ${DocumentTestFixtures.className()}.oneWithoutResourceId();

            // when: the item is saved
            ${EntityResource.className()} actualPojo = dataStoreUnderTest.create(pojoToSave);

            // expect: a non-null POJO with an assigned resourceId
            assertThat(actualPojo).isNotNull();
            assertThat(actualPojo.getResourceId()).isNotEmpty();
        }

        @Test
        void shouldThrowExceptionOnAttemptToSaveNullItem() {
            assertThrows(NullPointerException.class, () -> {
                dataStoreUnderTest.create(null);
            });
        }

        /**
         * Scenario: when {@code convert} is called to convert the POJO to a Document,
         * and the {@code convert} method returns null, expect a {@code NullPointerException}.
         */
        @Test
        void shouldThrowNullPointerExceptionWhenConversionFails() {
            ${PojoToDocumentConverter.className()} mockPojoToDocumentConverter = Mockito.mock(${PojoToDocumentConverter.className()}.class);

            // Create a DataStore that uses the mock converter
            ${MongoDataStore.className()} edgeCaseDataStore = ${MongoDataStoreProvider.className()}.builder()
                .documentConverter(documentToPojoConverter)
                .pojoConverter(mockPojoToDocumentConverter)
                .resourceIdGenerator(randomSeries)
                .mongoTemplate(mockMongoTemplate)
                .repository(mockRepository)
                .build();

            // given: the converter returns a null value
            given(mockPojoToDocumentConverter.convert(any(${EntityResource.className()}.class))).willReturn(null);

            assertThrows(NullPointerException.class, () -> {
                ${EntityResource.className()} result = edgeCaseDataStore.create(${DocumentTestFixtures.className()}.oneWithoutResourceId());
                });
        }
    }

    @Nested
    class DeleteByResourceId {
        @Test
        void shouldQuietlyRemoveExistingItem() {
            // scenario: the repository contains the record being deleted.
            // the first call to findBy is successful; the second call to findBy
            // comes up empty (the second call occurs after the delete operation;
            // attempts to find deleted records should return empty results)
            ${Document.className()} targetEntity = ${DocumentTestFixtures.className()}.oneWithResourceId();
            DeleteResult mockResult = Mockito.mock(DeleteResult.class);
            given(mockMongoTemplate.remove(any(Query.class), any(String.class))).willReturn(mockResult);
            given(mockResult.getDeletedCount()).willReturn(1L);

            // when
            String targetItem = targetEntity.getResourceId();
            dataStoreUnderTest.deleteByResourceId(targetItem);

            // expect: a non-null result.
            Optional<${EntityResource.className()}> result = dataStoreUnderTest.findByResourceId(targetItem);
            assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void shouldQuietlyIgnoreAttemptToDeleteNonExistentItem() {
            // scenario: the repository does not contain the record being deleted
            DeleteResult mockResult = Mockito.mock(DeleteResult.class);
            given(mockMongoTemplate.remove(any(Query.class), any(String.class))).willReturn(mockResult);
            given(mockResult.getDeletedCount()).willReturn(0L);

            String targetItem = "12345AbCdE56789xYz";
            // when
            dataStoreUnderTest.deleteByResourceId(targetItem);

            // expect
            Optional<${EntityResource.className()}> result = dataStoreUnderTest.findByResourceId(targetItem);
            assertThat(result).isNotNull().isEmpty();
        }

        @Test
        void shouldThrowExceptionWhenResourceIdIsNull() {
            assertThrows(NullPointerException.class, () -> {
                dataStoreUnderTest.deleteByResourceId(null);
            });
        }
    }

    @Nested
    class FindByText {
        @Test
        void shouldThrowExceptionWhenSearchTextIsNull() {
            Pageable page = PageRequest.of(0, 10);
            assertThrows(NullPointerException.class, () -> dataStoreUnderTest.findByText(null, page));
        }

        @Test
        void shouldReturnPageOfResults() {
            // given: the repository retrieves a page of entities that meet some criteria
            List<${Document.className()}> content = ${DocumentTestFixtures.className()}.allItems();
            Page<${Document.className()}> pageResult = new PageImpl<>(content, Pageable.unpaged(), content.size());
            given(mockRepository.findByTextContainingIgnoreCase(any(String.class), any(Pageable.class)))
            .willReturn(pageResult);

            // when: attempting a findByText
            Page<${EntityResource.className()}> actual = dataStoreUnderTest.findByText("anything", PageRequest.of(1, 10));

            //
            // expect: the result to contain as many items as were found in the repository.
            assertThat(actual).isNotNull().hasSize(content.size());
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteSuccessfully() {
            // Simulate the dataStore containing an item, such that requests
            // to delete the item are successful
            DeleteResult mockResult = Mockito.mock(DeleteResult.class);
            when(mockResult.getDeletedCount()).thenReturn(1L);
            when(mockMongoTemplate.remove(any(Query.class), any(String.class))).thenReturn(mockResult);

            // when: said item is deleted
            long count = dataStoreUnderTest.deleteByResourceId(${DocumentTestFixtures.className()}.sampleOne().getResourceId());

            // expect: the delete count is 1
            assertThat(count).isEqualTo(1);
        }
    }

    @Nested
    class Update {
        @Test
        void shouldReturnUpdatedVersionWhenItemExists() {
            // scenario: the repository contains the item being updated,
            // so the repository can find it and save it
            ${Document.className()} expectedDocument = ${DocumentTestFixtures.className()}.getSampleOne();
            ${EntityResource.className()} expectedPojo = documentToPojoConverter.convert(expectedDocument);
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            given(mockUpdateResult.getModifiedCount()).willReturn(1L);
            given(mockMongoTemplate.updateMulti(any(Query.class), any(), any(String.class)))
            .willReturn(mockUpdateResult);
            given(mockMongoTemplate.find(any(), any(), any(String.class))).willReturn(List.of(expectedDocument));

            // given
            List<${endpoint.pojoName}> resultList = dataStoreUnderTest.update(expectedPojo);

            assertThat(resultList).isNotNull();
            assertThat(resultList.size()).isGreaterThan(0);
            resultList.forEach(pojo -> {
            assertThat(pojo.getResourceId()).isEqualTo(expectedDocument.getResourceId());
            });
        }

        @Test
        void shouldReturnEmptyOptionWhenUpdatingNonExistentItem() {
            // scenario: an attempt is made to update an item that does not exist in the repository
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            given(mockUpdateResult.getModifiedCount()).willReturn(0L);
            given(mockMongoTemplate.updateMulti(any(Query.class), any(), any(String.class))).willReturn(mockUpdateResult);

            // given:
            List<${EntityResource.className()}> resultList = dataStoreUnderTest.update(${DocumentTestFixtures.className()}.oneWithResourceId());

            // expect: a non-null, but empty, result
            assertThat(resultList).isNotNull().isEmpty();
        }

        /**
         * This tests an edge case in timing. Our {@code update} implementation executes the update,
         * then fetches the items that were updated, to return to the caller.  Its possible for the
         * updated items to be deleted by another thread in between when the database update occurs and
         * the fetch of the updated items occurs. This test case verifies that edge case.
         */
        @Test
        void shouldReturnEmptyListWhenUpdatedItemVanishes() {
            // scenario: an attempt is made to update an item that does not exist in the repository
            ${Document.className()} expectedDocument = ${DocumentTestFixtures.className()}.getSampleOne();
            ${EntityResource.className()} expectedPojo = documentToPojoConverter.convert(expectedDocument);
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            given(mockUpdateResult.getModifiedCount()).willReturn(1L);
            given(mockMongoTemplate.updateMulti(any(Query.class), any(), any(String.class))).willReturn(mockUpdateResult);
            // exercise the scenario: the find() yields an empty list
            given(mockMongoTemplate.find(any(), any(), any(String.class))).willReturn(List.of());

            // given:
            List<${EntityResource.className()}> resultList = dataStoreUnderTest.update(${DocumentTestFixtures.className()}.oneWithResourceId());

            // expect: a non-null, but empty, result
            assertThat(resultList).isNotNull().isEmpty();
        }

        /*
         * Test the unusual scenario that the list of items updated cannot be converted
         * back into POJOs to be returned to the client.
         *
         * The set-up is a bit complicated, since we have to build a DataStoreProvider
         * that uses a (mock) converter that yields null items, along with all the other
         * mocked behavior.
         */
        @Test
        void shouldReturnEmptyListWhenConversionToPojoYieldsNullItems() {
            ${Document.className()} expectedDocument = ${DocumentTestFixtures.className()}.getSampleOne();
            ${EntityResource.className()} expectedPojo = documentToPojoConverter.convert(expectedDocument);
            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            given(mockUpdateResult.getModifiedCount()).willReturn(1L);
            given(mockMongoTemplate.updateMulti(any(Query.class), any(), any(String.class))).willReturn(mockUpdateResult);
            // exercise the scenario: the find() yields an empty list
            given(mockMongoTemplate.find(any(), any(), any(String.class))).willReturn(List.of());

            // given:
            List<${EntityResource.className()}> resultList = dataStoreUnderTest.update(${DocumentTestFixtures.className()}.oneWithResourceId());

            // expect: a non-null, but empty, result
            assertThat(resultList).isNotNull().isEmpty();
        }

        @Test
        void shouldThrowExceptionOnAttemptToUpdateNullItem() {
            assertThrows(NullPointerException.class, () -> {
                dataStoreUnderTest.update(null);
            });
        }

        /**
        * Note: this test has a fairly lengthy set-up to enable verifying a particular
        * edge case.
        */
        @Test
        void shouldReturnEmptyWhenConversionIsNull() {
            // Scenario: an update is applied but when the updated items are
            // converted into a List of domain objects to be returned to the caller,
            // the converter yields an empty list.
            ${MongoDataStoreProvider.className()} edgeCaseDataStore = aProviderWithAnIffyDocumentConverter();

            UpdateResult mockUpdateResult = Mockito.mock(UpdateResult.class);
            given(mockUpdateResult.getModifiedCount()).willReturn(1L);
            given(mockMongoTemplate.updateMulti(any(Query.class), any(), any(String.class)))
            .willReturn(mockUpdateResult);

            // given: the repository finds the requested record, but the conversion to a
            // POJO yields a null value
            ${EntityResource.className()} targetPojo = ${DocumentTestFixtures.className()}.oneWithResourceId();
            ${Document.className()} targetEjb = pojoToDocumentConverter.convert(targetPojo);
            assert targetEjb != null;
            when(mockMongoTemplate.find(any(), any(), any(String.class))).thenReturn(List.of(targetEjb));

            // when: an update of a specific Domain object is submitted...
            List<${EntityResource.className()}> resultList = edgeCaseDataStore.update(targetPojo);

            // then: in this edge case scenario, an empty list is returned.
            assertThat(resultList).isNotNull().isEmpty();
        }
    }

    // ==================================================================================================
    // HELPER METHODS
    // ==================================================================================================

    /**
    * Builds a Provider that uses a DocumentToPojo converter whose {@code convert}
    * method returns null.  Used for edge-case testing.
    */
    @SuppressWarnings("unchecked")
    private ${MongoDataStoreProvider.className()} aProviderWithAnIffyDocumentConverter() {
        ${DocumentToPojoConverter.className()} mockConverter = Mockito.mock(${DocumentToPojoConverter.className()}.class);
    when(mockConverter.convert(any(List.class))).thenReturn(null);

    return ${MongoDataStoreProvider.className()}.builder()
        .documentConverter(mockConverter)
        .pojoConverter(pojoToDocumentConverter)
        .resourceIdGenerator(randomSeries)
        .mongoTemplate(mockMongoTemplate)
        .repository(mockRepository)
        .build();
    }
}