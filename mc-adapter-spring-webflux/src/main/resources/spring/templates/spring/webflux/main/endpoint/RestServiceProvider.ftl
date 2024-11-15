<#include "/common/Copyright.ftl">

package ${ServiceImpl.packageName()};

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ${EntityResource.fqcn()};
import ${ConcreteDataStoreApi.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import ${ResourceNotFoundException.fqcn()};
import ${ServiceApi.fqcn()};

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class ${ServiceImpl.className()} implements ${ServiceApi.className()} {

    private final ${ConcreteDataStoreApi.className()} dataStore;

    /*
     * findAll
     */
    public Flux<${endpoint.pojoName}> findAll${endpoint.entityName}s() {
        return dataStore.findAll();
    }

    /**
     * findByResourceId
     */
    public Mono<${endpoint.pojoName}> findByResourceId(String id) throws ResourceNotFoundException {
        return dataStore.findByResourceId(id);
    }

    /*
     * findAllByText
     */
    public Flux<${endpoint.pojoName}> findAllByText(@NonNull String text) {
        return dataStore.findAllByText(text);
    }

    /**
     * Create
     */
    public Mono<String> create${endpoint.entityName}( @NonNull @Validated(OnCreate.class) ${endpoint.pojoName} resource ) {
        return dataStore.create${endpoint.entityName}(resource);
    }

    /**
     * Update
     */
    public Mono<${endpoint.entityName}> update${endpoint.entityName}( @NonNull @Validated(OnUpdate.class) ${endpoint.pojoName} resource ) {
        return dataStore.update${endpoint.entityName}(resource);
    }

    /**
     * Delete
     */
    public Mono<Long> delete${endpoint.entityName}ByResourceId(@NonNull String id) {
        return dataStore.deleteByResourceId(id);
    }
}
