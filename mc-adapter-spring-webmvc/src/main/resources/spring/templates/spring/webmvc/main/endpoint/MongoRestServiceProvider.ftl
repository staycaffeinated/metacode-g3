<#include "/common/Copyright.ftl">
package ${ServiceImpl.packageName()};

import ${Document.fqcn()};
import ${EntityResource.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@Validated
@RequiredArgsConstructor
public class ${ServiceImpl.className()} implements ${ServiceApi.className()} {

    private final ${MongoDataStore.className()} ${endpoint.lowerCaseEntityName}DataStore;

    /*
     * findAll
     */
    public List<${EntityResource.className()}> findAll${endpoint.entityName}s() {
        return ${endpoint.lowerCaseEntityName}DataStore.findAll();
    }

    /**
     * findByResourceId
     */
    public Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id) {
        return ${endpoint.lowerCaseEntityName}DataStore.findByResourceId(id);
    }

    /*
     * findByText
     */
    public Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable) {
        return ${endpoint.lowerCaseEntityName}DataStore.findByText(text, pageable);
    }

    /**
     * Persists a new resource
     */
    public ${EntityResource.className()} create${endpoint.entityName}(@NonNull @Validated(${OnCreateAnnotation.className()}.class) ${EntityResource.className()} resource) {
        return ${endpoint.lowerCaseEntityName}DataStore.create(resource);
    }

    /**
     * Updates an existing resource
     */
    public List<${EntityResource.className()}> update${endpoint.entityName}(@NonNull @Validated(${OnUpdateAnnotation.className()}.class) @Valid ${EntityResource.className()} resource) {
        return ${endpoint.lowerCaseEntityName}DataStore.update(resource);
    }

    /**
     * delete
     */
    public void delete${endpoint.entityName}ByResourceId(@NonNull String id) {
        ${endpoint.lowerCaseEntityName}DataStore.deleteByResourceId(id);
    }
}