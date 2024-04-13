<#include "/common/Copyright.ftl">
package ${endpoint.basePackage}.domain;

import ${endpoint.basePackage}.trait.ResourceIdTrait;
import ${endpoint.basePackage}.validation.Alphabetic;
import ${endpoint.basePackage}.validation.OnCreate;
import ${endpoint.basePackage}.validation.OnUpdate;
import ${endpoint.basePackage}.validation.ResourceId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
* This is the POJO of ${endpoint.entityName} data exposed to client applications
*/
@lombok.Data
// The next 2 lines allow jackson-databind and lombok to play nice together.
// These 2 lines specifically resolve this exception:
//    com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance...
// See https://www.thecuriousdev.org/lombok-builder-with-jackson/
@JsonDeserialize(builder = ${endpoint.pojoName}.DefaultBuilder.class)
@Builder(builderClassName = "DefaultBuilder", toBuilder = true)
public class ${endpoint.pojoName} implements ResourceIdTrait
<String> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Fields {
    public static final String RESOURCE_ID = "resourceId";
    public static final String TEXT = "text";
    }

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @ResourceId
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
    * Added to enable Lombok and jackson-databind to play nice together
    */
    @JsonPOJOBuilder(withPrefix = "")
    public static class DefaultBuilder {
    // empty
    }
    }