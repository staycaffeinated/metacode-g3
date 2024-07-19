<#include "/common/Copyright.ftl">

package ${ObjectDataStore.packageName()};

import ${Entity.fqcn()};
import ${DataStoreApi.fqcn()};
import reactor.core.publisher.Mono;

/**
 * This is the SPI for the ${endpoint.entityName} database adapter
 */
public interface ${ObjectDataStore.className()} extends ${DataStoreApi.className()}<${endpoint.entityName}> {
    Mono<String> create${endpoint.entityName}(${EntityResource.className()} pojo);
    Mono<${EntityResource.className()}> update${endpoint.entityName}(${EntityResource.className()} pojo);
}
