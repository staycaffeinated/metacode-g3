<#include "/common/Copyright.ftl">

package ${ObjectDataStoreProvider.packageName()};

import ${Entity.fqcn()};
import ${endpoint.basePackage}.database.${endpoint.lowerCaseEntityName}.predicate.*;
import ${EntityResource.fqcn()};
import ${SecureRandomSeries.fqcn()};
import ${ResourceIdSupplier.fqcn()};
import lombok.NonNull;
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
 * implementations of the CRUD operations, and implements ${DataStoreApi.className()} to enable
 * auto-wiring wherever a ${DataStoreApi.className()} is needed.
 */
@Component
public class ${ObjectDataStoreProvider.className()} extends GenericDataStore<${EntityResource.className()}, ${Entity.className()}, Long> implements ${endpoint.entityName}DataStore {

    /**
     * Constructor
     *
     * @param repository
     *            to handle I/O to the database
     * @param ejbToPojoConverter
     *            to convert EJBs to POJOs
     * @param pojoToEntityConverter
     *            to convert POJOs to EJBs
     * @param secureRandom
     *            to generate resourceIds
     */
    public ${ObjectDataStoreProvider.className()} (
            ${Repository.className()} repository,
            Converter<${Entity.className()},${EntityResource.className()}> ejbToPojoConverter,
            Converter<${EntityResource.className()}, ${Entity.className()}> pojoToEntityConverter,
            ResourceIdSupplier idSupplier)
    {
        super(repository, ejbToPojoConverter, pojoToEntityConverter, idSupplier);
    }

    @Override
    protected Optional<${Entity.className()}> findItem(${EntityResource.className()} domainObj) {
        return repository().findByResourceId(domainObj.getResourceId());
    }

    /**
     * Copies the applicable fields of the domain object, {@code from}, into the
     * database record, {@code to}.
     */
    @Override
    protected void applyBeforeUpdateSteps(${EntityResource.className()} from, ${Entity.className()} to) {
        to.setText(from.getText());
    }

    /**
     * Copies the applicable fields of the domain object, {@code from}, into the
     * database record, {@code to}.
     */
    @Override
    protected void applyBeforeInsertSteps(${EntityResource.className()} from, ${Entity.className()} to) {
        to.setResourceId(super.nextResourceId());
        this.applyBeforeUpdateSteps(from, to);
    }

    /**
     * Returns a Page of ${endpoint.entityName} items that have the given {@code text}
     */
    public Page<${EntityResource.className()}> findByText(@NonNull Optional<String> text, Pageable pageable) {
        Specification<${Entity.className()}> where = Specification.where(new ${EntityResource.className()}WithText(text.orElse("")));
        Page<${Entity.className()}> resultSet = repository().findAll(where, pageable);
        List<${EntityResource.className()}> list = resultSet.stream().map(converterToPojo()::convert).toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}




