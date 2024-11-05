<#include "/common/Copyright.ftl">

package ${GenericDataStore.packageName()};

import ${CustomRepository.fqcn()};
import ${UpdateAwareConverter.fqcn()};
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class provides default implementation of CRUD operations for a
 * DataStore. A DataStore is, basically, a wrapper around a Repository. The
 * DataStore implements any business rules that need to happen when
 * reading/writing from a Repository. The DataStore mainly exposes Domain
 * objects, and encapsulates the EntityBeans and Repository.
 * <p>
 * {@code D} is the Domain object type</p>
 * <p>{@code B} is the EntityBean type</p>
 * <p>{@code ID} is the primary key data type (e.g., Long or String)</p>
 *
 * For example {@code GenericDataStore<Pet,PetEntity,Long>}.
 */
@SuppressWarnings("java:S119") // 'ID' mimics Spring convention
public abstract class ${GenericDataStore.className()}<D,B,ID> {
<#noparse>
    @Value("${application.default-page-limit:25}")
</#noparse>
    private int defaultPageLimit;

    private static final int DEFAULT_ROW_LIMIT = 20;

    private final ${CustomRepository.className()}<B,ID> repository;
    private final Converter<B, D> ejbToPojoConverter;
    private final ${UpdateAwareConverter.className()}<D, B> pojoToEjbConverter;

    protected ${GenericDataStore.className()}(${CustomRepository.className()}<B,ID> repository,
                                              Converter<B, D> ejbToPojoConverter,
                                              ${UpdateAwareConverter.className()}<D, B> pojoToEjbConverter)
    {
        this.repository = repository;
        this.ejbToPojoConverter = ejbToPojoConverter;
        this.pojoToEjbConverter = pojoToEjbConverter;
    }

    /**
     * Returns a handle to the Repository that's enabling the DataStore to
     * read/write to the database.
     */
    protected ${CustomRepository.className()}<B,ID> repository() { return repository; }

    /**
     * Returns the Converter used to convert an entity bean into a domain object
     */
    protected Converter<B, D> converterToPojo() { return ejbToPojoConverter; }

    /**
     * Returns the Converter used to convert domain objects into (unmanaged) entity
     * beans.
     */
    protected Converter<D, B> converterToEjb() { return pojoToEjbConverter; }

    /**
     * Retrieve an EJB having the given {@code resourceId}
     *
     * @param resourceId the public identifier of the entity
     * @return an Optional that contains either the desired EJB or is empty
     */
    public Optional<D> findByResourceId(@NonNull String resourceId) {
        Optional<B> optional = repository.findByResourceId(resourceId);
            if (optional.isPresent()) {
                D pojo = ejbToPojoConverter.convert(optional.get());
                if (Objects.nonNull(pojo))
                return Optional.of(pojo);
            }
        return Optional.empty();
    }

    public List<D> findAll() {
        if (defaultPageLimit <= 0)
            defaultPageLimit = DEFAULT_ROW_LIMIT;
        return repository.findAll().stream().limit(defaultPageLimit).map(ejbToPojoConverter::convert).toList();
    }

    public List<D> findAll(int limit) {
        if (limit <= 0)
            limit = DEFAULT_ROW_LIMIT;
        return repository.findAll().stream().limit(limit).map(ejbToPojoConverter::convert).toList();
    }
                
    public void deleteByResourceId(@NonNull String resourceId) {
        Optional<B> optional = repository.findByResourceId(resourceId);
            if (optional.isPresent()) {
                B ejb = optional.get();
                repository.delete(ejb);
        }
    }

    /**
     * Fetch the EJB corresponding to {@code item}. A basic implementation will look
     * similar to: <code>
     *     Optional findItem(Some item) {
     *         return repository().findByResourceId(item.getResourceId());
     *     }
     * </code>
     *
     * @param item the Domain object being sought in the database
     * @return an Optional containing the corresponding EJB or, if not found, empty.
     */
    protected abstract Optional<B> findItem(D item);

    /**
     * Persist the changes to `item` as an update (not an insert).
     * If `item` is not found in the database, this request is quietly ignored.
     */
    public Optional<D> update(D item) {
            Optional<B> optional = findItem(item);
            if (optional.isPresent()) {
                B ejb = optional.get();
                pojoToEjbConverter.copyUpdates(item, ejb);
                B managed = repository().save(ejb);
                D updated = converterToPojo().convert(managed);
                if (updated != null)
                    return Optional.of(updated);
            }
        return Optional.empty();
    }

    public D save(@NonNull D item) {
        B ejb = converterToEjb().convert(item);
        if (ejb != null) {
            B managedEntity = repository().save(ejb);
            return converterToPojo().convert(managedEntity);
        }
        return null;
    }
}