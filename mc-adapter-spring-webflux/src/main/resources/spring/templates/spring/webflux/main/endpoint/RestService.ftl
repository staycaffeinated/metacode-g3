<#include "/common/Copyright.ftl">

package ${ServiceApi.packageName()};

import ${EntityResource.fqcn()};
import ${ResourceNotFoundException.fqcn()};

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${ServiceApi.className()} {

    /*
     * findAll
     */
    Flux<${EntityResource.className()}> findAll${endpoint.entityName}s();

    /*
     * findAllByText
     */
    Flux<${EntityResource.className()}> findAllByText(String text);

    /**
     * Create
     */
    Mono<String> create${endpoint.entityName}(${EntityResource.className()} resource );

    /**
     * Update
     */
    Mono<${EntityResource.className()}> update${endpoint.entityName}(${EntityResource.className()} resource );

    /**
     * Delete
     */
    Mono<Long> delete${endpoint.entityName}ByResourceId(String id);

    /**
     * Find the POJO having the given resourceId
     */
    Mono<${EntityResource.className()}> findByResourceId(String id) throws ${ResourceNotFoundException.className()};

}
