<#include "/common/Copyright.ftl">

package ${ObjectDataStoreProvider.packageName()};

import ${BadRequestException.fqcn()};
import ${Entity.fqcn()};
import ${EntityWithText.fqcn()};
import ${EntitySpecification.fqcn()};
import ${EntityResource.fqcn()};
import ${GenericDataStore.fqcn()};
import ${ObjectDataStore.fqcn()};
import ${Repository.fqcn()};
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
 * implementations of the CRUD operations, and implements ${DataStoreApi.className()} to enable
 * auto-wiring wherever a ${DataStoreApi.className()} is needed.
 */
@Component
public class ${ObjectDataStoreProvider.className()} extends ${GenericDataStore.className()}<${EntityResource.className()}, ${Entity.className()}, Long> implements ${ObjectDataStore.className()} {

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
    public ${ObjectDataStoreProvider.className()} (
            ${Repository.className()} repository,
            Converter<${Entity.className()},${EntityResource.className()}> ejbToPojoConverter,
            Converter<${EntityResource.className()}, ${Entity.className()}> pojoToEntityConverter)
    {
        super(repository, ejbToPojoConverter, pojoToEntityConverter);
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
        this.applyBeforeUpdateSteps(from, to);
    }

    /**
     * Returns a Page of ${endpoint.entityName} items that have the given {@code text}
     */
    public Page<${EntityResource.className()}> findByText(@NonNull Optional<String> text, Pageable pageable) {
        Specification<${Entity.className()}> where = ${EntitySpecification.className()}.builder().text(text.orElse("")).build();
        Page<${Entity.className()}> resultSet = repository().findAll(where, pageable);
        List<${EntityResource.className()}> list = resultSet.stream().map(converterToPojo()::convert).toList();
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
                resultSet = repository().findAll(pageable);
            }
            else {
                Specification<${Entity.className()}> where = RSQLJPASupport.toSpecification(searchQuery);
                resultSet = repository().findAll(where, pageable);
            }
            List<${EntityResource.className()}> list = resultSet.stream().map(converterToPojo()::convert).toList();
            return new PageImpl<>(list, pageable, list.size());
        }
        catch (RSQLParserException e) {
            throw new BadRequestException(e.getLocalizedMessage());
        }
    }
}




