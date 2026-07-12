<#include "/common/Copyright.ftl">

package ${ConcreteDocumentStoreImpl.packageName()};

import ${DocumentToPojoConverter.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${ConcreteDocumentStoreApi.fqcn()};
import com.mongodb.client.result.UpdateResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
* Default implementation of the ${endpoint.entityName}DataStore.
*/
@Component
@RequiredArgsConstructor
public class ${ConcreteDocumentStoreImpl.className()} implements ${ConcreteDocumentStoreApi.className()} {

    private final ${DocumentToPojoConverter.className()} documentConverter;
    private final ${PojoToDocumentConverter.className()} pojoConverter;
    private final ${ResourceIdSupplier.className()} resourceIdGenerator;
    private final MongoTemplate mongoTemplate;
    private final ${Repository.className()} repository;

    private static final String RESOURCE_ID = ${Document.className()}.Columns.RESOURCE_ID;

    @Override
    public Optional<${EntityResource.className()}> findByResourceId(@NonNull String publicId)  {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(publicId));
        ${Document.className()} document = mongoTemplate.findOne(query, ${Document.className()}.class, ${Document.className()}.collectionName());
        if (document == null) return Optional.empty();
        return Optional.of(documentConverter.convert(document));
    }

    @Override
    public List<${endpoint.entityName}> findAll() {
        // @formatter:off
        return mongoTemplate.findAll(${Document.className()}.class, ${Document.className()}.collectionName())
                            .stream()
                            .map(documentConverter::convert)
                            .toList();
        // @formatter:on
    }

    @Override
    public ${EntityResource.className()} create(${EntityResource.className()} pojo) {
        ${Document.className()} document = pojoConverter.convert(pojo);
        document.setResourceId(resourceIdGenerator.nextResourceId());
        ${Document.className()} managed = mongoTemplate.save(document, ${Document.className()}.collectionName());
        return documentConverter.convert(managed);
    }

    @Override
    public List<${EntityResource.className()}> update(${EntityResource.className()} pojo) {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(pojo.getResourceId()));
        // By default, this is only updating the 'text' column.
        // You will want to decide how you want this to actually work and change this.
        UpdateDefinition updateDefinition = Update.update(${Document.className()}.Columns.TEXT, pojo.getText());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, updateDefinition, ${Document.className()}.collectionName());
        if (updateResult.getModifiedCount() == 0) return List.of();
        List<${Document.className()}> modified = mongoTemplate.find(query, ${Document.className()}.class, ${Document.className()}.collectionName());
        if (modified.isEmpty()) return List.of();
        return List.copyOf(documentConverter.convert(modified));
    }

    @Override
    public Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable) {
        Page<${Document.className()}> rs = repository.findByTextContainingIgnoreCase(text, pageable);
        List<${EntityResource.className()}> list = rs.stream().map(documentConverter::convert).toList();
        return new PageImpl<>(list, pageable, rs.getTotalElements());
    }

    @Override
    public long deleteByResourceId(@NonNull String resourceId) {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(resourceId));
        return mongoTemplate.remove(query, ${Document.className()}.collectionName()).getDeletedCount();
    }
}