<#include "/common/Copyright.ftl">

package ${ServiceApi.packageName()};

import ${EntityResource.fqcn()};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ${ServiceApi.className()} {
    /*
     * findAll
     */
    List<${EntityResource.className()}> findAll${endpoint.entityName}s();

    /**
     * findByResourceId
     */
    Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id);

    /*
     * findByText
     */
    Page<${EntityResource.className()}> findByText(Optional<String> text, Pageable pageable);

    /**
     * Persists a new resource
     */
    ${EntityResource.className()} create${endpoint.entityName}(${endpoint.pojoName} resource );

    /**
     * Updates an existing resource
     */
    Optional<${EntityResource.className()}> update${endpoint.entityName}(${endpoint.pojoName} resource );

    /**
     * delete
     */
    void delete${endpoint.entityName}ByResourceId( String id );
}
