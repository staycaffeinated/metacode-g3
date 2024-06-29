<#include "/common/Copyright.ftl">

package ${Entity.packageName()};

import ${SecureRandomSeries.fqcn()};
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
<#if endpoint.schema?has_content>
    @Table(name="${endpoint.tableName}", schema="${endpoint.schema}")
<#else>
    @Table(name="${endpoint.tableName}")
</#if>
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
        public static final String RESOURCE_ID = "resource_id";
        public static final String TEXT = "text";
    }

    /*
    * This identifier is never exposed to the outside world because
    * database-generated Ids are commonly sequential values that a hacker can easily guess.
    */
    @Id
    <#if (endpoint.isWithPostgres())>
    @SequenceGenerator(name = "${Entity.varName()}_sequence", sequenceName = "${Entity.varName()}_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${endpoint.lowerCaseEntityName}_sequence")
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

    @PrePersist
    public void prePersist() {
        resourceId = ${SecureRandomSeries.className()}.instance().nextResourceId();
    }
}