/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.endpoint.converter;

import java.util.function.Predicate;
import mmm.coffee.metacode.common.base.PredicateHelper;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.CatalogEntryPredicates;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.trait.ConvertTrait;

/**
 * RestEndpointDescriptorToPredicateConverter
 */
public class RestEndpointDescriptorToPredicateConverter implements ConvertTrait<RestEndpointDescriptor, Predicate<CatalogEntry>> {
    /**
     * Converts an instance of class {@code FROM} into an instance of class {@code TO}.
     * We let the implementer decide how to handle {@code nulls}
     *
     * @param fromType some instance to convert
     * @return the transformed object
     */
    @Override
    public Predicate<CatalogEntry> convert(RestEndpointDescriptor fromType) {
        if (fromType.isWebFlux()) {
            // isEndpointTemplate AND isWebFluxTemplate
            return PredicateHelper.and(CatalogEntryPredicates.isEndpointArtifact(),
                    CatalogEntryPredicates.isWebFluxArtifact());
        } else {
            // isEndpointTemplate and isWebMvcTemplate
            return PredicateHelper.and(CatalogEntryPredicates.isEndpointArtifact(),
                    CatalogEntryPredicates.isWebMvcArtifact());
        }
    }
}
