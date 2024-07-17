<#include "/common/Copyright.ftl">

package ${ObjectDataStoreProvider.packageName()};

import ${PojoToEntityConverter.fqcn()};
import ${EntityToPojoConverter.fqcn()};
import ${EntityResource.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import ${Repository.fqcn()};
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * An Adapter for the persistence of ${endpoint.entityName}s
 */
@Component
@Builder
@RequiredArgsConstructor
@Slf4j
public class ${ObjectDataStoreProvider.className()} implements ${ObjectDataStore.className()} {
    private final ${Repository.className()} repository;
    private final ${EntityToPojoConverter.className()} ejbToPojoConverter;
    private final ${PojoToEntityConverter.className()} pojoToEjbConverter;
    private final ${ResourceIdSupplier.className()} resourceIdSupplier;

    /**
     * create
     */
    public Mono<String> create${endpoint.entityName}(${endpoint.pojoName} pojo) {
        ${endpoint.ejbName} entity = pojoToEjbConverter.convert(pojo);
		    if (entity == null) {
			      return Mono.error(new UnprocessableEntityException());
  		  }
        entity.setResourceId(resourceIdSupplier.nextResourceId());
        return repository.save(entity).flatMap(item -> Mono.just(item.getResourceId()));
    }

    /**
     * update
     */
    public Mono<${endpoint.entityName}> update${endpoint.entityName}(${endpoint.pojoName} resource) {
		    return repository.findByResourceId(resource.getResourceId())
				    .switchIfEmpty(Mono.error(new ResourceNotFoundException()))
    				.flatMap(found -> repository.save(found.copyMutableFieldsFrom(resource)))
		    		.mapNotNull(ejbToPojoConverter::convert);
    }

    /**
     * findByResourceId
     */
    @Override
    public Mono<${endpoint.pojoName}> findByResourceId(String id) {
        Mono<${endpoint.ejbName}> monoItem = repository.findByResourceId(id) .switchIfEmpty(Mono.defer(() -> Mono.error(
                    new ResourceNotFoundException(String.format("Entity not found with the given resource ID: %s", id)))));
        return monoItem.flatMap(it -> Mono.just(Objects.requireNonNull(ejbToPojoConverter.convert(it))));
    }

    /**
     * findById
     */
    @Override
    public Mono<${endpoint.pojoName}> findById(Long id) {
        Mono<${endpoint.ejbName}> monoItem = repository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(
            new ResourceNotFoundException(String.format("Entity not found with the given database ID: %s", id)))));
        return monoItem.flatMap(it -> Mono.just(Objects.requireNonNull(ejbToPojoConverter.convert(it))));
    }

    /**
     * deleteByResourceId 
     * @return the number of rows deleted
     */
    @Override
    public Mono<Long> deleteByResourceId(String id) {
        return repository.deleteByResourceId(id);
    }

    /**
     * findAllByText
     */
    @Override
    public Flux<${endpoint.pojoName}> findAllByText(String text) {
        return Flux.from(repository.findAllByText(text).mapNotNull(ejbToPojoConverter::convert));
    }

    /**
     * findAll
     */
    public Flux<${endpoint.pojoName}> findAll() {
        return Flux.from(repository.findAll().mapNotNull(ejbToPojoConverter::convert));
    }
}
