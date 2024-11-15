<#include "/common/Copyright.ftl">

package ${ConcreteDataStoreImpl.packageName()};

import ${PojoToEntityConverter.fqcn()};
import ${EntityToPojoConverter.fqcn()};
import ${EntityResource.fqcn()};
import ${UnprocessableEntityException.fqcn()};
import ${ResourceNotFoundException.fqcn()};
import ${Repository.fqcn()};
import ${ConcreteDataStoreApi.fqcn()};
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

/**
 * An Adapter for the persistence of ${endpoint.entityName}s
 */
@Component
@Builder
@RequiredArgsConstructor
@Slf4j
public class ${ConcreteDataStoreImpl.className()} implements ${ConcreteDataStoreApi.className()} {
    private final ${Repository.className()} repository;
    private final ${EntityToPojoConverter.className()} ejbToPojoConverter;
    private final ${PojoToEntityConverter.className()} pojoToEjbConverter;

    /**
     * create
     */
    public Mono<String> create${endpoint.entityName}(${endpoint.pojoName} pojo) {
        ${endpoint.ejbName} entity = pojoToEjbConverter.convert(pojo);
		if (entity == null) {
			      return Mono.error(new UnprocessableEntityException());
  		}
        return repository.save(entity).flatMap(item -> Mono.just(item.getResourceId()));
    }

    /**
     * Update the item. Returns either a Mono containing the updated item, or an empty Mono.
     */
    public Mono<${endpoint.entityName}> update${endpoint.entityName}(${endpoint.pojoName} resource) {
	    return repository.findByResourceId(resource.getResourceId())
                    .map(Optional::of)
				    .defaultIfEmpty(Optional.empty())
                    .flatMap(optionalItem -> {
                        if (optionalItem.isPresent()) {
                            ${Entity.className()} ejb = optionalItem.get();
                            ejb.copyMutableFieldsFrom(resource);
                            return repository.save(ejb).mapNotNull(ejbToPojoConverter::convert);
                        }
                        return Mono.empty();
                    });
    }

    /**
     * findByResourceId.
     * @return  a Mono containing the requested item or Mono.empty() if the item was not found
     */
    @Override
    public Mono<${endpoint.pojoName}> findByResourceId(String id) {
        Mono<${endpoint.ejbName}> monoItem = repository.findByResourceId(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalItem -> optionalItem
                    .<Mono<? extends ${Entity.className()}>>map(Mono::just) // if found
                    .orElseGet(Mono::empty)); // if not found
        return monoItem.flatMap(it -> Mono.just(Objects.requireNonNull(ejbToPojoConverter.convert(it))));
    }

    /**
     * findById
     */
    @Override
    public Mono<${endpoint.pojoName}> findById(Long id) {
        Mono<${endpoint.ejbName}> monoItem = repository.findById(id)
                    .map(Optional::of)
                    .defaultIfEmpty(Optional.empty())
                    .flatMap(optionalItem -> optionalItem
                        .<Mono<? extends ${Entity.className()}>>map(Mono::just) // if found
                        .orElseGet(Mono::empty));  // if not found
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
