<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

import lombok.NonNull;
import ${EntityResource.fqcn()};
import ${ConcreteDataStoreApi.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ${ServiceImpl.className()} implements ${ServiceApi.className()} {

    private final ${ConcreteDataStoreApi.className()} ${ConcreteDataStoreApi.varName()};

    /*
     * Constructor
     */
    public ${ServiceImpl.className()}(${ConcreteDataStoreApi.className()} ${ConcreteDataStoreApi.varName()})
    {
      this.${ConcreteDataStoreApi.varName()} = ${ConcreteDataStoreApi.varName()};
    }

    /*
     * findAll
     */
    public List<${EntityResource.className()}> findAll${endpoint.entityName}s() {
        return ${ConcreteDataStoreApi.varName()}.findAll();
    }

    /**
     * findByResourceId
     */
    public Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id) {
        return ${ConcreteDataStoreApi.varName()}.findByResourceId ( id );
    }

    /*
     * findByText
     */
    public Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable) {
        return ${ConcreteDataStoreApi.varName()}.findByText(text, pageable);
    }

    /*
     * Search
     */
    public Page<${EntityResource.className()}> search(@NonNull String rsqlQuery, Pageable pageable) {
        return ${ConcreteDataStoreApi.varName()}.search(rsqlQuery, pageable);
    }

    /**
    * Persists a new resource
    */
    public ${EntityResource.className()} create${endpoint.entityName}( @NonNull @Validated(OnCreate.class) ${endpoint.pojoName} resource ) {
        return ${ConcreteDataStoreApi.varName()}.save(resource);
    }

    /**
     * Updates an existing resource
     */
    public Optional<${endpoint.pojoName}> update${endpoint.entityName}(@NonNull @Validated(OnUpdate.class) ${endpoint.pojoName} resource ) {
        return ${ConcreteDataStoreApi.varName()}.update(resource);
    }

    /**
     * delete
     */
    public void delete${endpoint.entityName}ByResourceId(@NonNull String id) {
        ${ConcreteDataStoreApi.varName()}.deleteByResourceId(id);
    }
}
