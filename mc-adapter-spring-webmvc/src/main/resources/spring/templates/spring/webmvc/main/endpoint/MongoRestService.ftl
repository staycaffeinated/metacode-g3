<#include "/common/Copyright.ftl">
package ${ServiceApi.packageName()};

import ${EntityResource.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface ${ServiceApi.className()} {

    /*
     * findAll
     */
    List<${endpoint.entityName}> findAll${endpoint.entityName}s();

    /**
     * findByResourceId
     */
    Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id);

    /*
     * findByText
     */
    Page<${EntityResource.className()}> findByText(@NonNull String text, Pageable pageable);

    /**
     * Persists a new resource
     */
    ${EntityResource.className()} create${endpoint.entityName}(@NonNull @Validated(${OnCreateAnnotation.className()}.class) ${EntityResource.className()} resource);

    /**
     * Updates an existing resource
     */
    List<${EntityResource.className()}> update${endpoint.entityName}(@NonNull @Validated(${OnUpdateAnnotation.className()}.class) @Valid ${EntityResource.className()} resource);

    /**
     * delete
     */
    void delete${endpoint.entityName}ByResourceId(@NonNull String id);
}