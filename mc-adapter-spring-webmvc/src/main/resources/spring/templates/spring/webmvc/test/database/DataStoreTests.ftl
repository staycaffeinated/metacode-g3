<#include "/common/Copyright.ftl">

package ${ObjectDataStoreProvider.packageName()};

import ${BadRequestException.fqcn()};
import ${ObjectDataStore.fqcn()};
import ${EntityToPojoConverter.fqcn()};
import ${PojoToEntityConverter.fqcn()};
import ${EntityResource.fqcn()};
import ${Repository.fqcn()};
import ${WebMvcModelTestFixtures.fqcn()};
import cz.jirutka.rsql.parser.RSQLParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * Unit test the ${ObjectDataStoreProvider.className()}
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
public class ${ObjectDataStoreProvider.testClass()} {

    @Mock
    ${Repository.className()} mockRepository;

    ${EntityToPojoConverter.className()} ejbToPojoConverter = new ${EntityToPojoConverter.className()}();
    ${PojoToEntityConverter.className()} pojoToEjbConverter = new ${PojoToEntityConverter.className()}();
    ${ObjectDataStoreProvider.className()} dataStoreUnderTest;

    @BeforeEach
    void setUp() {
        dataStoreUnderTest = new ${ObjectDataStoreProvider.className()}(mockRepository, ejbToPojoConverter, pojoToEjbConverter);
    }

    @Nested
    class FindByResourceId {
        @Test
        void shouldFindOne() {
            // scenario: an item is know to exist in the database
            Optional<${Entity.className()}> optional = Optional.of(${WebMvcEjbTestFixtures.className()}.oneWithResourceId());
            String expectedResourceId = ${WebMvcEjbTestFixtures.className()}.oneWithResourceId().getResourceId();
            when(mockRepository.findByResourceId(any(String.class))).thenReturn(optional);

            // when: the dataStore is asked to find the known-to-exist item by its publicId...
            Optional<${EntityResource.className()}> actualResult = dataStoreUnderTest.findByResourceId(expectedResourceId);

            assertThat(actualResult).isNotNull().isPresent();
            assertThat(actualResult.get().getResourceId()).isEqualTo(expectedResourceId);
        }

        @Test
        void shouldReturnEmptyWhenNoMatchingRecord() {
            // scenario: the repository cannot find any such record...
            Optional<${Entity.className()}> optional = Optional.empty();
            given(mockRepository.findByResourceId(any(String.class))).willReturn(optional);

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
            // Create a Mock ejbToPojoConverter to enable testing a particular branch of code
            ${EntityToPojoConverter.className()} mockEjbToPojoConverter = Mockito.mock(${EntityToPojoConverter.className()}.class);

            // Create a DataStore that uses the mock converter
            ${ObjectDataStore.className()} storeUnderTest = new ${ObjectDataStoreProvider.className()}(
                                mockRepository, mockEjbToPojoConverter, pojoToEjbConverter);

            // given: the repository is able to find a particular entity
            // but when the entity is converted to a POJO, a NULL value is returned
            ${Entity.className()} targetEntity = ${WebMvcEjbTestFixtures.className()}.oneWithResourceId();
            given(mockRepository.findByResourceId(any(String.class))).willReturn(Optional.of(targetEntity));
            given(mockEjbToPojoConverter.convert(any(${Entity.className()}.class))).willReturn(null);

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
            given(mockRepository.findAll()).willReturn(${WebMvcEjbTestFixtures.className()}.allItems());

            // when:
            List<${EntityResource.className()}> items = dataStoreUnderTest.findAll();

            // expect: the resultSet contains the same number of items as were found in the
            // repository
            assertThat(items).isNotNull().isNotEmpty().hasSize(${WebMvcEjbTestFixtures.className()}.allItems().size());
        }

        @Test
        void shouldHaveEmptyResults() {
            // scenario: the repository contains no ${EntityResource.className()} entities
            given(mockRepository.findAll()).willReturn(List.of());

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
            // scenario: a ${EntityResource.className()} is successfully saved in the repository, and assigned a resourceId.
            // Under the covers, the EJB is assigned a resourceId, then saved, so the record
            // returned by the repository should also have a resourceId. When the EJB is
            // transformed into a POJO, the POJO should retain the resourceId
            ${Entity.className()} savedEntity = ${WebMvcEjbTestFixtures.className()}.oneWithResourceId();
            given(mockRepository.save(any(${Entity.className()}.class))).willReturn(savedEntity);
            ${EntityResource.className()} pojoToSave = ${WebMvcModelTestFixtures.className()}.oneWithoutResourceId();

            // when: the item is saved
            ${EntityResource.className()} actualPojo = dataStoreUnderTest.save(pojoToSave);

            // expect: a non-null POJO with an assigned resourceId
            assertThat(actualPojo).isNotNull();
            assertThat(actualPojo.getResourceId()).isNotEmpty();
        }

        @Test
        void shouldThrowExceptionOnAttemptToSaveNullItem() {
            assertThrows(NullPointerException.class, () -> {
                dataStoreUnderTest.save(null);
            });
        }

        @Test
        void shouldReturnNullWhenConverterReturnsNull() {
            ${PojoToEntityConverter.className()} mockPojoToEntityConverter = Mockito.mock(${PojoToEntityConverter.className()}.class);

            // Create a DataStore that uses the mock converter
            ${ObjectDataStore.className()} edgeCaseDataStore = new ${ObjectDataStoreProvider.className()}(
                                mockRepository, ejbToPojoConverter, mockPojoToEntityConverter);

            // given: the converter returns a null value
            given(mockPojoToEntityConverter.convert(any(${EntityResource.className()}.class))).willReturn(null);

            // when:
            ${EntityResource.className()} result = edgeCaseDataStore.save(${WebMvcModelTestFixtures.className()}.oneWithoutResourceId());

            // expect: when the conversion goes side-ways, save() returns a null
            assertThat(result).isNull();
        }
    }

    @Nested
    class Update {
        @Test
        void shouldReturnUpdatedVersionWhenItemExists() {
            // scenario: the repository contains the item being updated,
            // so the repository can find it and save it
            ${EntityResource.className()} testSubject = ${WebMvcModelTestFixtures.className()}.oneWithResourceId();
            ${Entity.className()} persistedSubject = pojoToEjbConverter.convert(testSubject);
            assert persistedSubject != null;
            given(mockRepository.findByResourceId(any(String.class))).willReturn(Optional.of(persistedSubject));
            given(mockRepository.save(any(${Entity.className()}.class))).willReturn(persistedSubject);

            // given
            Optional<${EntityResource.className()}> option = dataStoreUnderTest.update(testSubject);

            // expect: the Optional contains the updated item.
            // Since resourceIds are immutable, expect a match on the value before and after
            assertThat(option).isNotNull().isPresent();
            assertThat(option.get().getResourceId()).isEqualTo(testSubject.getResourceId());
        }

        @Test
        void shouldReturnEmptyOptionWhenUpdatingNonExistentItem() {
            // scenario: an attempt is made to update an item that does not exist in the
            // repository
            given(mockRepository.findByResourceId(any(String.class))).willReturn(Optional.empty());

            // given:
            Optional<${EntityResource.className()}> option = dataStoreUnderTest.update(${WebMvcModelTestFixtures.className()}.oneWithResourceId());

            // expect: a non-null, but empty, result
            assertThat(option).isNotNull().isEmpty();
        }

        @Test
        void shouldThrowExceptionOnAttemptToUpdateNullItem() {
            assertThrows(NullPointerException.class, () -> {
                dataStoreUnderTest.update(null);
            });
        }

        @Test
        void shouldReturnEmptyWhenConversionIsNull() {
            // Create a Mock ejbToPojoConverter to enable testing a particular branch of code
            ${EntityResource.className()}EntityToPojoConverter mockEjbToPojoConverter = Mockito.mock(${EntityResource.className()}EntityToPojoConverter.class);
            given(mockEjbToPojoConverter.convert(any(${Entity.className()}.class))).willReturn(null);
            // Create a DataStore that uses the mock converter
            ${EntityResource.className()}DataStore edgeCaseDataStore = new ${EntityResource.className()}DataStoreProvider(mockRepository, mockEjbToPojoConverter, pojoToEjbConverter);

            // given: the repository finds the requested record, but the conversion to a
            // POJO yields a null value
            ${EntityResource.className()} targetPojo = ${WebMvcModelTestFixtures.className()}.oneWithResourceId();
            ${Entity.className()} targetEjb = pojoToEjbConverter.convert(targetPojo);
            assert targetEjb != null;
            given(mockRepository.findByResourceId(any(String.class))).willReturn(Optional.of(targetEjb));
            given(mockRepository.save(any(${Entity.className()}.class))).willReturn(targetEjb);

            // when: an update is attempted
            Optional<${EntityResource.className()}> optional = edgeCaseDataStore.update(targetPojo);

            // expect: an empty Optional is returned
            assertThat(optional).isNotNull().isEmpty();
        }
    }

    @Nested
    class DeleteByResourceId {
        @Test
        void shouldQuietlyRemoveExistingItem() {
            ${Entity.className()} targetEntity = ${WebMvcEjbTestFixtures.className()}.oneWithResourceId();
            // scenario: the repository contains the record being deleted.
            // the first call to findBy is successful; the second call to findBy
            // comes up empty (the second call occurs after the delete operation;
            // attempts to find deleted records should return empty results)
            // @formatter:off
            given(mockRepository.findByResourceId(any(String.class)))
                                .willReturn(Optional.of(targetEntity)) // happens when delete is called
                                .willReturn(Optional.empty()); // happens after delete is called
            // @formatter:on
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
            given(mockRepository.findByResourceId(any(String.class))).willReturn(Optional.empty());

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
        @SuppressWarnings("unchecked")
        void shouldReturnPageOfResults() {
            // given: the repository retrieves a page of entities that meet some criteria
            List<${Entity.className()}> content = ${WebMvcEjbTestFixtures.className()}.allItems();
            Page<${Entity.className()}> pageResult = new PageImpl<>(content, Pageable.unpaged(), content.size());
            given(mockRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(pageResult);

            // when: attempting a findByText
            Page<${EntityResource.className()}> actual = dataStoreUnderTest.findByText("anything", PageRequest.of(1, 10));

            // expect: the result to contain as many items as were found in the repository.
            assertThat(actual).isNotNull().hasSize(content.size());
        }
    }

    @Nested
    @SuppressWarnings("unchecked")
    class SearchUseCases {
        @Test
        void shouldThrowExceptionIfQueryIsNull() {
            assertThrows(NullPointerException.class, () -> dataStoreUnderTest.search(null, PageRequest.of(1, 10)));
        }

        @Test
        void shouldReturnAnyItemsWhenQueryIsEmpty() {
            List<${Entity.className()}> content = ${WebMvcEjbTestFixtures.className()}.allItems();
            Page<${Entity.className()}> pageResult = new PageImpl<>(content, Pageable.unpaged(), content.size());
            when(mockRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

            Page<${EntityResource.className()}> results = dataStoreUnderTest.search("", PageRequest.of(1, 10));
            assertThat(results).isNotNull().isNotEmpty();
        }

        @Test
        void shouldReturnMatchingItemsWhenQueryIsGiven() {
            // given: an RSQL query against a known column
            List<${Entity.className()}> content = ${WebMvcEjbTestFixtures.className()}.allItemsWithSameText();
            Page<${Entity.className()}> pageResult = new PageImpl<>(content, Pageable.unpaged(), content.size());
            when(mockRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageResult);

            // Build a query from some known value (known to exist in the database)
            String rsqlQuery = ${Entity.className()}.Columns.TEXT + "==" + content.get(0).getText();

            // when: the data is searched with the rsql query...
            Page<${EntityResource.className()}> results = dataStoreUnderTest.search(rsqlQuery, PageRequest.of(1, 10));

            // expect results to come back, and the records to satisfy the search criteria
            // (this is more to verify the implementation is right, not so much to verify the RSQL library).
            assertThat(results).isNotNull().isNotEmpty();
            Predicate<${EntityResource.className()}> predicate = (item) -> item.getText().equals(content.get(0).getText());
            assertThat(results.get().allMatch(predicate)).isTrue();
        }

        @Test
        void shouldMapRSQLExceptionToBadRequestException() {
            when(mockRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenThrow(RSQLParserException.class);

            assertThrows(BadRequestException.class, () -> dataStoreUnderTest.search("anything", PageRequest.of(1, 10)));
        }
    }
}
