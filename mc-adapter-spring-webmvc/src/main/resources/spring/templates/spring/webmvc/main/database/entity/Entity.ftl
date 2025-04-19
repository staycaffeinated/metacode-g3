<#include "/common/Copyright.ftl">

package ${Entity.packageName()};

import ${SecureRandomSeries.fqcn()};
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity(name="${endpoint.tableName}")
@EqualsAndHashCode(of = {"resourceId"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings({
    "java:S125" // comments do contain example code
})
public class ${Entity.className()} {

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
<#if (endpoint.isWithPostgres())>
     *
     * When using Postgres, the data type of the 'id' column needs to align with
     * the `GenerationType`.  Specifically, for GenerationType.IDENTITY, the 'id'
     * needs to be 'serial' (or a compatible type) while for GenerationType.SEQUENCE,
     * the `id` needs to be `int8` (or a compatible type).
     *
     * For GenerationType.IDENTITY, the JPA should follow this pattern:
     * ```
     * @Id
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     * @Column(name = Columns.ID, nullable = false, updatable = false, columnDefinition = "serial")
     * private Long id;
     * ```
     * with the DDL something like:
     * ```create table if not exists ${endpoint.tableName} (id serial primary key, ...)```
     *
     * For GenerationType.SEQUENCE, the JPA should follow this pattern:
     * ```
     * @Id
     * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${endpoint.tableName}_generator")
     * @SequenceGenerator(name="${endpoint.tableName}_generator", sequenceName = "${endpoint.tableName}_sequence", allocationSize=50)
     * @Column(name = Columns.ID, updatable = false, nullable = false)
     * private Long id;
     * ```
     * with DDL something like this:
     * ```create sequence if not exists ${endpoint.tableName}_sequence start 1 increment 50;```
     * where the `allocationSize` attribute of the `@SequenceGenerator` matches the `increment` in the DDL.
     *
     * See: https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/
</#if>
     *
     */
    @Id
    <#if (endpoint.isWithPostgres())>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Columns.ID, columnDefinition = "serial")
    <#else>
    @GeneratedValue
    @Column(name=Columns.ID)
    </#if>
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

    @PrePersist
    public void prePersist() {
        resourceId = ${SecureRandomSeries.className()}.instance().nextResourceId();
    }
}