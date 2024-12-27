<#include "/common/Copyright.ftl">

package ${Entity.packageName()};

import ${SecureRandomSeries.fqcn()};
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Table(name="${endpoint.tableName}")
@EqualsAndHashCode(of = {"resourceId"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
     *
     * In the DDL, the ID is declared as "id SERIAL PRIMARY KEY".
     * Postgres automatically handles assigning sequential values to Serial columns.
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