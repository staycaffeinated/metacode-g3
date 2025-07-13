<#include "/common/Copyright.ftl">
package ${EntityResource.packageName()};

import ${ResourceIdAnnotation.fqcn()};
import ${AlphabeticAnnotation.fqcn()};
import ${OnCreateAnnotation.fqcn()};
import ${OnUpdateAnnotation.fqcn()};
import ${ResourceIdTrait.fqcn()};
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This is the POJO of ${endpoint.entityName} data exposed to client applications
 *
 * If you migrate this class to use a lombok `@Builder`, you will want the lombok
 * annotation to be `@Builder(builderClassName="DefaultBuilder", toBuilder=true);`
 * This enables lombok and jackson-databind to play nice together.
 * Specifically, it avoids the exception
 * `com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance...`
 * See https://www.thecuriousdev.org/lombok-builder-with-jackson/
 */
@JsonDeserialize(builder = ${endpoint.pojoName}.DefaultBuilder.class)
public class ${EntityResource.className()} implements ${ResourceIdTrait.className()}<String> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Fields {
        public static final String RESOURCE_ID = "resourceId";
        public static final String TEXT = "text";
    }

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @${ResourceIdAnnotation.className()}
    @JsonProperty(Fields.RESOURCE_ID)
    private String resourceId;

    /*
     * An @Pattern can also be used here.
     * The @Alphabet is used to demonstrate a custom validator
     */
    @NotEmpty @Alphabetic
    @JsonProperty(Fields.TEXT)
    private String text;

    /**
     * Returns a Builder for this class
     */
    public static Builder builder() { return new DefaultBuilder(); }

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


    /*
    * The Builder interface
    */
    public interface Builder {
        Builder resourceId(String resourceId);

        Builder text(String text);

        ${EntityResource.className()} build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DefaultBuilder implements Builder {
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
        public ${EntityResource.className()} build() {
            ${EntityResource.className()} pojo = new ${EntityResource.className()}();
            pojo.resourceId = resourceId;
            pojo.text = text;
            return pojo;
        }
    }
}