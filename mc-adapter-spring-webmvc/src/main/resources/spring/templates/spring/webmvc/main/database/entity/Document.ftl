<#include "/common/Copyright.ftl">

package ${Document.packageName()};

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(${Document.className()}.COLLECTION_NAME)
@EqualsAndHashCode(of = {"resourceId"})
@NoArgsConstructor
@AllArgsConstructor
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

    /**
     * Returns a builder for this document class
     */
    public static Builder builder() {
        return new DefaultBuilder();
    }

    /*
     * Getters and Setters. Once you settle on variable names,
     * move to Lombok if you wish. The code generator starts
     * with sample instance variables which most likely are not
     * what you want. When an instance variable is renamed,
     * the lombok get/set methods frequently don't get updated
     * everywhere in the code to reflect the name change, leading
     * to compile errors. With explicit get/set methods, IDEs
     * easily refactor name changes.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    /**
     * The builder interface
     */
    public interface Builder {
        Builder resourceId(String resourceId);

        Builder text(String text);

        ${Document.className()} build();
    }

    /*
     * The implementation of the Builder
     */
    private static class DefaultBuilder implements Builder {
        private String resourceId;
        private String text;

        @Override
        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        @Override
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public ${Document.className()} build() {
            ${Document.className()} doc = new ${Document.className()}();
            doc.resourceId = resourceId;
            doc.text = text;
            return doc;
        }
    }
}