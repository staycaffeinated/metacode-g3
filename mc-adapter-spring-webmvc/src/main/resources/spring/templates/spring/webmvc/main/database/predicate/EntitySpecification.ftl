<#include "/common/Copyright.ftl">
package ${EntitySpecification.packageName()};

import ${Entity.fqcn()};
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;


/**
 * A Builder to create Specification's for database searches.
 * Use the builder to set the column values for the search;
 * the resulting Specification will be an `and` of those column values.
 */
@Builder
@Data
public class ${EntitySpecification.className()} implements Specification<${Entity.className()}> {

    // This is a 'user-defined' UID; feel free to improve it.
    private static final long serialVersionUID = 1L;

    /*
     * The general idea is to add any of the entity's columns you want to be able
     * to search. For each instance variable here, also add a mini-predicate
     * for that instance variable in the `toPredicate` method. Basically, follow
     * the pattern that's already started.
     */
    private final String resourceId;
    private final String text;


    @Override
    public Predicate toPredicate(@NonNull Root<${Entity.className()}> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {

        Predicate resourceIdPred = ofNullable(resourceId)
            .map(bool -> isEqualTo(cb, root.get(${Entity.className()}.Columns.RESOURCE_ID), resourceId))
            .orElse(null);

        Predicate textPred = ofNullable(text)
            .map(bool -> isEqualTo(cb, root.get(${Entity.className()}.Columns.TEXT), text))
            .orElse(null);

        List<Predicate> predicates = new ArrayList<>();

        // Add the non-null predicates to the list
        ofNullable(resourceIdPred).ifPresent(predicates::add);
        ofNullable(textPred).ifPresent(predicates::add);

        // Conjure a predicate that is, essentially, p1 and p2 and p3 ...
        // where p1,p2,p3 are predicates from the list
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate isEqualTo(CriteriaBuilder cb, Path<Object> field, Object value) {
        return cb.equal(field, value);
    }
}

