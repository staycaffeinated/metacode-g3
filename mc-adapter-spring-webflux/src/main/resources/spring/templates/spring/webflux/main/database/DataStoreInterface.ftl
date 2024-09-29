<#include "/common/Copyright.ftl">

package ${ConcreteDataStoreApi.packageName()};

import ${EntityResource.fqcn()};
import ${AbstractDataStoreApi.fqcn()};
import reactor.core.publisher.Mono;

/**
 * This is the "service provider interface" (spi) for the ${endpoint.entityName} database adapter.
 * The difference between this and repository interfaces is this interface interacts with POJOs
 * while repository interfaces interact with entity beans.
 */
public interface ${ConcreteDataStoreApi.className()} extends ${AbstractDataStoreApi.className()}<${EntityResource.className()}> {
    Mono<String> create${endpoint.entityName}(${EntityResource.className()} pojo);
    Mono<${EntityResource.className()}> update${endpoint.entityName}(${EntityResource.className()} pojo);
}
