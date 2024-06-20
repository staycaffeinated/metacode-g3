<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

import lombok.NonNull;
import ${EntityResource.fqcn()};
import ${ObjectDataStore.fqcn()};
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

    private final ${ObjectDataStore.className()} ${ObjectDataStore.varName()};

    /*
     * Constructor
     */
    public ${ServiceImpl.className()}(${ObjectDataStore.className()} ${ObjectDataStore.varName()})
    {
      this.${ObjectDataStore.varName()} = ${ObjectDataStore.varName()};
    }

    /*
     * findAll
     */
    public List<${EntityResource.className()}> findAll${endpoint.entityName}s() {
        return ${ObjectDataStore.varName()}.findAll();
    }

    /**
     * findByResourceId
     */
    public Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id) {
        return ${ObjectDataStore.varName()}.findByResourceId ( id );
    }

    /*
     * findByText
     */
    public Page<${EntityResource.className()}> findByText(@NonNull Optional<String> text, Pageable pageable) {
        return ${ObjectDataStore.varName()}.findByText(text, pageable);
    }

    /**
    * Persists a new resource
    */
    public ${EntityResource.className()} create${endpoint.entityName}( @NonNull @Validated(OnCreate.class) ${endpoint.pojoName} resource ) {
        return ${ObjectDataStore.varName()}.save(resource);
    }

    /**
     * Updates an existing resource
     */
    public Optional<${endpoint.pojoName}> update${endpoint.entityName}(@NonNull @Validated(OnUpdate.class) ${endpoint.pojoName} resource ) {
        return ${ObjectDataStore.varName()}.update(resource);
    }

    /**
     * delete
     */
    public void delete${endpoint.entityName}ByResourceId(@NonNull String id) {
        ${ObjectDataStore.varName()}.deleteByResourceId(id);
    }
}
