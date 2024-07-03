<#include "/common/Copyright.ftl">

package ${Repository.packageName()};

import ${RegisterDatabaseProperties.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Document.fqcn()};
import ${DocumentTestFixtures.fqcn()};
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests to verify query syntax. The general idea is, if custom queries are added
 * to the Repository interface, then leverage this class to verify the queries
 * and the query syntax.
 */
@DataMongoTest
class ${Repository.integrationTestClass()} implements ${RegisterDatabaseProperties.className()} {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ${Repository.className()} repository;

    @BeforeEach
    void populateDatabaseWithTestData() {
        repository.saveAll(${DocumentTestFixtures.className()}.allItems());
    }

    @AfterEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldFindAll() {
        int expectedSize = ${DocumentTestFixtures.className()}.allItems().size();
        assertThat((long) repository.findAll().size()).isEqualTo(expectedSize);
    }

    @Test
    void shouldBeAbleToSaveAndFind() {
        var savedDoc = repository.save(${DocumentTestFixtures.className()}.sampleOne());
        assertThat(repository.findById(savedDoc.getId())).isPresent();
    }

    @Test
    void shouldSupportMongoTemplate() {
        mongoTemplate.createCollection(${Document.className()}.collectionName());
        mongoTemplate.insert(${DocumentTestFixtures.className()}.allItems(), ${Document.className()}.collectionName());

        String expectedResourceId = ${DocumentTestFixtures.className()}.sampleOne().getResourceId();
        Query query = new Query();
        query.addCriteria(Criteria.where("resourceId").is(expectedResourceId));

        List<${Document.className()}> resultSet = mongoTemplate.find(query, ${Document.className()}.class);
        assertThat(resultSet.get(0).getResourceId()).isEqualTo(expectedResourceId);
    }
}