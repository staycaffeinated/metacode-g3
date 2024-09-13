<#include "/common/Copyright.ftl">

package ${DocumentKindStoreProvider.packageName()};

import ${DocumentToPojoConverter.fqcn()};
import ${PojoToDocumentConverter.fqcn()};
import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${DocumentKindStore.fqcn()};
import com.mongodb.client.result.UpdateResult;
import lombok.Builder;
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
@Builder
public class ${DocumentKindStoreProvider.className()} implements ${DocumentKindStore.className()} {

    private final ${DocumentToPojoConverter.className()} documentConverter;
    private final ${PojoToDocumentConverter.className()} pojoConverter;
    private final ${ResourceIdSupplier.className()} resourceIdGenerator;
    private final MongoTemplate mongoTemplate;
    private final ${Repository.className()} repository;

    private static final String RESOURCE_ID = "resourceId";

    @Override
    public Optional<${EntityResource.className()}> findByResourceId(@NonNull String publicId)  {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(publicId));
        ${Document.className()} document = mongoTemplate.findOne(query, ${Document.className()}.class, ${Document.className()}.collectionName());
        if (document == null)
            return Optional.empty();
        else {
            ${EntityResource.className()} pojo = documentConverter.convert(document);
            return Optional.ofNullable(pojo);
        }
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
        Objects.requireNonNull(document);
        document.setResourceId(resourceIdGenerator.nextResourceId());
        ${Document.className()} managed = mongoTemplate.save(document, ${Document.className()}.collectionName());
        return documentConverter.convert(managed);
    }

    @Override
    public List<${EntityResource.className()}> update(${EntityResource.className()} pojo) {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(pojo.getResourceId()));
        // By default, this is only updating the 'text' column.
        // You will want to decide how you want this to actually work and change this.
        UpdateDefinition updateDefinition = Update.update("text", pojo.getText());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, updateDefinition, ${Document.className()}.collectionName());
        if (updateResult.getModifiedCount() > 0) {
            List<${Document.className()}> modified = mongoTemplate.find(query, ${Document.className()}.class, ${Document.className()}.collectionName());
            if (!modified.isEmpty()) {
                List<${EntityResource.className()}> pojoList = documentConverter.convert(modified);
                return List.copyOf(pojoList);
            }
        }
        // No matches were found, so return an empty list
        return List.of();
    }

    @Override
    public Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable) {
        Page<${Document.className()}> rs = repository.findByTextContainingIgnoreCase(text, pageable);
        List<${EntityResource.className()}> list = rs.stream().map(documentConverter::convert).toList();
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public long deleteByResourceId(@NonNull String resourceId) {
        Query query = Query.query(Criteria.where(RESOURCE_ID).is(resourceId));
        return mongoTemplate.remove(query, ${Document.className()}.collectionName()).getDeletedCount();
    }
}