/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.project.mustache;

import lombok.*;
import mmm.coffee.metacode.common.mustache.MustacheExpressionResolver;
import mmm.coffee.metacode.common.trait.DecodeTrait;
import mmm.coffee.metacode.spring.project.converter.RestTemplateModelToMapConverter;
import mmm.coffee.metacode.spring.project.model.RestProjectTemplateModel;

import java.util.Map;

/**
 * This function decodes any mustache expression used in
 * the {@code CatalogEntry.destination} field. Typically,
 * this means translating "{{basePackagePath}}" to a file-system
 * style path such as, say, "org/acme/petstore".
 */
@Builder
public class MustacheDecoder implements DecodeTrait {
    /*
     * This map does not need to be visible to anything.
     * This map is used to cache results
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Map<String, String> map;

    private RestTemplateModelToMapConverter converter;

    /**
     * Initialize a map to be used by the Mustache parser to resolve values
     *
     * @param model the source data that's read to create the map
     */
    public void configure(RestProjectTemplateModel model) {
        map = converter.convert(model);
    }

    public String decode(@NonNull String incoming) {
        return MustacheExpressionResolver.resolve(incoming, map);
    }
}
