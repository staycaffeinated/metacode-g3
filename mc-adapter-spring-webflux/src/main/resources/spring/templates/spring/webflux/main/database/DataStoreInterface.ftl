<#include "/common/Copyright.ftl">

package ${ObjectDataStore.packageName()};

import ${Entity.fqcn()};
import ${DataStore.fqcn()};
import reactor.core.publisher.Mono;

/**
 * This is the SPI for the ${endpoint.entityName} database adapter
 */
public interface ${ObjectDataStore.className()} extends ${DataStore.className()}<${endpoint.entityName}> {
    Mono<String> create${endpoint.entityName}(${endpoint.entityName} pojo);
    Mono<${endpoint.entityName}> update${endpoint.entityName}(${endpoint.entityName} pojo);
}
