<#include "/common/Copyright.ftl">

package ${ConcreteDataStoreImpl.packageName()};

import ${BadRequestException.fqcn()};
import ${Entity.fqcn()};
import ${EntitySpecification.fqcn()};
import ${EntityResource.fqcn()};
import ${GenericDataStore.fqcn()};
import ${ConcreteDataStoreApi.fqcn()};
import ${Repository.fqcn()};
import ${UpdateAwareConverter.fqcn()};
import lombok.NonNull;
import cz.jirutka.rsql.parser.RSQLParserException;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * ${EntityResource.className()} DataStore provider. This extends the CrudDataStore to inherit the
 * implementations of the CRUD operations, and implements ${AbstractDataStoreApi.className()} to enable
 * auto-wiring wherever a ${AbstractDataStoreApi.className()} is needed.
 */
@Component
public class ${ConcreteDataStoreImpl.className()} implements ${ConcreteDataStoreApi.className()} {

    // Default number of rows per page
    private static final int DEFAULT_ROW_LIMIT = 20;

    private final ${Repository.className()} repository;
    private final Converter<${Entity.className()},${EntityResource.className()}> mapEjbToPojo;
    private final ${UpdateAwareConverter.className()}<${EntityResource.className()}, ${Entity.className()}> mapPojoToEjb;

    /**
     * Constructor
     *
     * @param repository
     *            to handle I/O to the database
     * @param ejbToPojoConverter
     *            to convert EJBs to POJOs
     * @param pojoToEntityConverter
     *            to convert POJOs to EJBs
     */
    public ${ConcreteDataStoreImpl.className()} (
            ${Repository.className()} repository,
            Converter<${Entity.className()},${EntityResource.className()}> ejbToPojoConverter,
            ${UpdateAwareConverter.className()}<${EntityResource.className()}, ${Entity.className()}> pojoToEntityConverter)
    {
        this.repository = repository;
        this.mapEjbToPojo = ejbToPojoConverter;
        this.mapPojoToEjb = pojoToEntityConverter;
    }

    @Override
    public Optional<${EntityResource.className()}> findByResourceId(@NonNull String resourceId) {
        return repository.findByResourceId(resourceId).map(mapEjbToPojo::convert);
    }

    /**
     * Returns a Page of ${endpoint.entityName} items that have the given {@code attributeValue}
     */
    public Page<${EntityResource.className()}> findByAttribute(@NonNull String attributeValue, Pageable pageable) {
        Specification<${Entity.className()}> where = ${EntitySpecification.className()}.builder().text(attributeValue).build();
        Page<${Entity.className()}> resultSet = repository.findAll(where, pageable);
        List<${EntityResource.className()}> list = resultSet.stream().map(mapEjbToPojo::convert).toList();
        return new PageImpl<>(list, pageable, list.size());
    }

    /**
     * Returns a Page of ${endpoint.entityName} items that satisfy the {@code searchQuery}
     * The search query is expressed in RSQL; see
     * <a href="https://github.com/perplexhub/rsql-jpa-specification">RSQL Query Syntax</a> for query syntax
     */
    public Page<${EntityResource.className()}> search(@NonNull String searchQuery, Pageable pageable) {
        try {
            Page<${Entity.className()}> resultSet;
            if (searchQuery.isEmpty()) {
                resultSet = repository.findAll(pageable);
            }
            else {
                Specification<${Entity.className()}> where = RSQLJPASupport.toSpecification(searchQuery);
                resultSet = repository.findAll(where, pageable);
            }
            List<${EntityResource.className()}> list = resultSet.stream().map(mapEjbToPojo::convert).toList();
            return new PageImpl<>(list, pageable, list.size());
        }
        catch (RSQLParserException e) {
            throw new BadRequestException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<${EntityResource.className()}> findAll() {
        return repository.findAll().stream()
            .limit(pageLimit(DEFAULT_ROW_LIMIT))
            .map(mapEjbToPojo::convert)
            .toList();
    }

    public List<${EntityResource.className()}> findAll(int limit) {
        return repository.findAll().stream()
            .limit(pageLimit(limit))
            .map(mapEjbToPojo::convert)
        .toList();
    }

    @Override
    public ${EntityResource.className()} save(@NonNull ${EntityResource.className()} item) {
        ${Entity.className()} ejb = mapPojoToEjb.convert(item);
        if (ejb != null) {
            ${Entity.className()} managedEntity = repository.save(ejb);
            return mapEjbToPojo.convert(managedEntity);
        }
        return null;
    }

    @Override
    public Optional<${EntityResource.className()}> update(${EntityResource.className()} item) {
        Optional<${Entity.className()}> optional = repository.findByResourceId(item.getResourceId());
        if (optional.isPresent()) {
            ${Entity.className()} ejb = optional.get();
            mapPojoToEjb.copyUpdates(item, ejb);
            ${Entity.className()} managed = repository.save(ejb);
            ${EntityResource.className()} updated = mapEjbToPojo.convert(managed);
            if (updated != null) return Optional.of(updated);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByResourceId(@NonNull String resourceId) {
        repository.findByResourceId(resourceId).ifPresent(repository::delete);
    }

    protected int pageLimit(int preferredLimit) {
        return preferredLimit > 0 ? preferredLimit : DEFAULT_ROW_LIMIT;
    }

}




