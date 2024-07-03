<#include "/common/Copyright.ftl">

package ${Document.packageName()};

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(${Document.className()}.COLLECTION_NAME)
@EqualsAndHashCode(of = {"resourceId"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${Document.className()} {

    public static final String COLLECTION_NAME = "${endpoint.tableName}";

    public static String collectionName() {
        return COLLECTION_NAME;
    }

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
    @Column(name=Columns.ID)
    private String id;

    /**
     * This is the identifier exposed to the outside world.
     * This is a secure random value with at least 160 bits of entropy, making it difficult for a hacker to guess.
     * This is a unique value in the database. This value can be a positive or negative number.
     */
    @Column(name=Columns.RESOURCE_ID, nullable=false)
    private String resourceId;

    @Column(name=Columns.TEXT, nullable = true)
    private String text;
}