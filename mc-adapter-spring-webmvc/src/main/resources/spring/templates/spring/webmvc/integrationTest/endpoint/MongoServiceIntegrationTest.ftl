<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

// import ${endpoint.basePackage}.database.MongoDbContainerSupport;
import ${Container.fqcn()};
import ${Document.fqcn()};
import ${DocumentToPojoConverter.fqcn()};
import ${EntityResource.fqcn()};
import ${ServiceApi.fqcn()};
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
class ${ServiceImpl.integrationTestClass()} extends MongoDbContainerSupport {

    @Autowired
    ${DataStoreApi.className()} ${endpoint.lowerCaseEntityName}DataStore;

    // The repository is directly accessed to enable removing all test data
    @Autowired
    ${Repository.className()} ${endpoint.lowerCaseEntityName}Repository;

    ${DocumentToPojoConverter.className()} ${endpoint.lowerCaseEntityName}DocumentToPojoConverter = new ${DocumentToPojoConverter.className()}();

    @Autowired
    MongoTemplate mongoTemplate;

    private ${ServiceImpl.className()} serviceUnderTest;

    @BeforeEach
    void setUp() {
        ${endpoint.lowerCaseEntityName}Repository.deleteAll();
        serviceUnderTest = new ${ServiceImpl.className()}(${endpoint.lowerCaseEntityName}DataStore);
        mongoTemplate.insertAll(${DocumentTestFixtures.className()}.allItems());
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.remove(${endpoint.entityName}Document.class);
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindAll() {
            Set<${endpoint.entityName}> results = serviceUnderTest.findAll${endpoint.entityName}s();
            assertThat(results).isNotNull();
        }
    }
}