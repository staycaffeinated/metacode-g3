<#include "/common/Copyright.ftl">

package ${Entity.packageName()};

import ${SecureRandomSeries.fqcn()};
import ${EntityResource.fqcn()};
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;


@Table(name="${endpoint.tableName}")
@EqualsAndHashCode(of = {"resourceId"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${Entity.className()} implements Persistable<Long> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Columns {
        public static final String ID = "id";
        // Be aware some databases want column names in camelCase, some want snake_case.
        public static final String RESOURCE_ID = "resourceId";
        public static final String TEXT = "text";
    }

    /*
     * This identifier is never exposed to the outside world because
     * database-generated Ids are commonly sequential values that a hacker can easily guess.
     */
    @Id
    <#if (endpoint.isWithPostgres())>
    @SequenceGenerator(name = "${endpoint.lowerCaseEntityName}_generator", sequenceName = "${endpoint.lowerCaseEntityName}_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${endpoint.lowerCaseEntityName}_generator")
    <#else>
    @GeneratedValue
    </#if>
    @Column(name=Columns.ID)
    private Long id;

    /**
     * This is the identifier exposed to the outside world.
     * This is a secure random value with at least 160 bits of entropy, making it difficult for a hacker to guess.
     * This is a unique value in the database. This value can be a positive or negative number.
     */
    @Column(name=Columns.RESOURCE_ID, nullable=false)
    private String resourceId;

    @Column(name=Columns.TEXT, nullable = false)
    @NotEmpty(message = "Text cannot be empty")
    private String text;

    @Override
    public boolean isNew() {
        return id == null;
    }

    /**
     * This gets invoked by the ${Entity.className()}Callback::onBeforeSave method.
     * R2DBC does not support @PrePersist, so a callback is used for the same effect.
     */
    public void beforeInsert() {
        resourceId = ${SecureRandomSeries.className()}.instance().nextResourceId();
    }

    public ${Entity.className()} copyMutableFieldsFrom(${EntityResource.className()} pojo) {
        this.text = pojo.getText();
        return this;
    }

}