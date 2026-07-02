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
        return repository.save(entity).map(${endpoint.ejbName}::getResourceId);
    }

    /**
     * Update the item. Returns either a Mono containing the updated item, or an empty Mono.
     */
    public Mono<${endpoint.entityName}> update${endpoint.entityName}(${endpoint.pojoName} resource) {
	    return repository.findByResourceId(resource.getResourceId())
                    .flatMap(ejb -> {
                        pojoToEjbConverter.copyUpdates(resource, ejb); // copy changes into ejb
                        return repository.save(ejb).mapNotNull(ejbToPojoConverter::convert);
                    });
    }

    /**
     * findByResourceId.
     * @return  a Mono containing the requested item or Mono.empty() if the item was not found
     */
    @Override
    public Mono<${endpoint.pojoName}> findByResourceId(String id) {
        return repository.findByResourceId(id).flatMap(entity -> {
                ${endpoint.pojoName} pojo = ejbToPojoConverter.convert(entity);
                if (pojo == null) {
                    return Mono.error(new ResourceNotFoundException());
                }
                return Mono.just(pojo);
        });
    }

    /**
     * findById
     */
    @Override
    public Mono<${endpoint.pojoName}> findById(Long id) {
        return repository.findById(id).flatMap(entity -> {
                ${endpoint.pojoName} pojo = ejbToPojoConverter.convert(entity);
                if (pojo == null) {
                    return Mono.error(new ResourceNotFoundException());
                }
                return Mono.just(pojo);
        });
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
        return repository.findAllByText(text).mapNotNull(ejbToPojoConverter::convert);
    }

    /**
     * findAll
     */
    public Flux<${endpoint.pojoName}> findAll() {
        return repository.findAll().mapNotNull(ejbToPojoConverter::convert);
    }
}
