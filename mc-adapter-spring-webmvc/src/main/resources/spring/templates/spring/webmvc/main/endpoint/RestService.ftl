<#include "/common/Copyright.ftl">

package ${ServiceApi.packageName()};

import ${EntityResource.fqcn()};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ${ServiceApi.className()} {

    List<${EntityResource.className()}> findAll${endpoint.entityName}s();

    Optional<${EntityResource.className()}> find${endpoint.entityName}ByResourceId(String id);

    Page<${EntityResource.className()}> findByAttribute(String attributeValue, Pageable pageable);

    Page<${EntityResource.className()}> search(String rsqlQuery, Pageable pageable);

    ${EntityResource.className()} create${endpoint.entityName}(${endpoint.pojoName} resource );

    Optional<${EntityResource.className()}> update${endpoint.entityName}(${endpoint.pojoName} resource );

    Optional<${EntityResource.className()}> delete${endpoint.entityName}ByResourceId( String id );
}
