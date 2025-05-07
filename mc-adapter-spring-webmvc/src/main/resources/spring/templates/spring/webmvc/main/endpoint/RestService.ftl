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
     * findByAttribute
     */
    Page<${EntityResource.className()}> findByAttribute(String attributeValue, Pageable pageable);

    /**
     * Search for elements with an RSQL query
     */
    Page<${EntityResource.className()}> search(String rsqlQuery, Pageable pageable);


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
