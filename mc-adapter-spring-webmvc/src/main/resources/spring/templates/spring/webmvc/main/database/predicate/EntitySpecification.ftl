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

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;


/**
 * A Builder to create Specification's for database searches.
 * Use the builder to set the column values for the search;
 * the resulting Specification will be an `and` of those column values.
 */
@SuppressWarnings({
    "java:S125" // allow block comments
})
@Builder
public class ${EntitySpecification.className()} implements Specification<${Entity.className()}> {

    // This is a 'user-defined' UID; feel free to improve it.
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * The general idea is to add any of the entity's columns you want to be able
     * to search. For each instance variable here, also add a mini-predicate
     * for that instance variable in the `toPredicate` method. Basically, follow
     * the pattern that's already started.
     */
    private final String resourceId;
    private final String text;

    /*
     * Set this to `true` to enable the condition `[text-column] is null`; in the Builder API,
     * it would be `${Entity.className()}.builder().textIsNull(true).build();`
     * Remember, `text` is merely a default attribute added to the entity. More than likely,
     * you'll want to refactor and name the property something else. If, for instance,
     * the attribute `text` is renamed to `email`, then remember to change `text` here to `email`
     * to enable the Builder API to auto-generate method names like `emailIsLike` and `emailIsNull`.
     */
    private final boolean textIsNull;

    /*
     * Assign a value to this to add the condition `[text-column] is like [textIsLike]`
     * The caller must add '%' as needed to obtain the desired semantics; for example:
     * `...builder().textIsLike("%Value")` or
     * `...builder().textIsLike("Value%")` or
     * `...builder().textIsLike("_Value");
     */
    private final String textIsLike;


    @Override
    public Predicate toPredicate(@NonNull Root<${Entity.className()}> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {

        Predicate pResourceId = ofNullable(resourceId)
            .map(bool -> isEqualTo(cb, root.get(${Entity.className()}.Columns.RESOURCE_ID), resourceId))
            .orElse(null);

        Predicate pText = ofNullable(text)
            .map(bool -> isEqualTo(cb, root.get(${Entity.className()}.Columns.TEXT), text))
            .orElse(null);

        Predicate pIsNullText = textIsNull
            ? isNullValue(cb, root.get(${Entity.className()}.Columns.TEXT))
            : null;

        Predicate pIsTextLike = ofNullable(textIsLike)
            .map(bool -> isLikeValue(cb, root.get(PetEntity.Columns.TEXT), textIsLike))
            .orElse(null);

        List<Predicate> predicates = new ArrayList<>();

        // Add the non-null predicates to the list
        ofNullable(pResourceId).ifPresent(predicates::add);
        ofNullable(pText).ifPresent(predicates::add);
        ofNullable(pIsNullText).ifPresent(predicates::add);
        ofNullable(pIsTextLike).ifPresent(predicates::add);

        // Conjure a predicate that is, essentially, p1 and p2 and p3 ...so on...
        // where p1,p2,p3 are predicates from the list
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate isEqualTo(CriteriaBuilder cb, Path<Object> field, Object value) {
        return cb.equal(field, value);
    }

    private Predicate isNullValue(CriteriaBuilder cb, Path<Object> field) {
       return cb.isNull(field);
    }

    private Predicate isLikeValue(CriteriaBuilder cb, Path<Object> field, String value) {
        // This also casts the column value to String to enable applying the LIKE condition
        return cb.like(field.as(String.class), value);
    }
}

