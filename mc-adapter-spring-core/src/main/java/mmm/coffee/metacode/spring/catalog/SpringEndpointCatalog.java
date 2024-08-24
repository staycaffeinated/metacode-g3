/*
 * Copyright (c) 2022 Jon Caulfield.
 */
package mmm.coffee.metacode.spring.catalog;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import mmm.coffee.metacode.common.catalog.TemplateCatalog;
import mmm.coffee.metacode.common.descriptor.Descriptor;
import mmm.coffee.metacode.common.descriptor.RestEndpointDescriptor;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.stereotype.Collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SpringEndpointCatalog
 */
@Builder
@RequiredArgsConstructor
@Slf4j
public class SpringEndpointCatalog implements Collector {

    final ICatalogReader reader;
    // To hide this field from the Builder, we limit both the getter and setter
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final List<String> appliedCatalogs = new ArrayList<>();

    @Override
    public Collector prepare(Descriptor descriptor) {
        // Select the catalog to apply based on the framework and database flavor
        if (descriptor instanceof RestEndpointDescriptor restEndpointDescriptor) {
            if (restEndpointDescriptor.isWebFlux()) {
                appliedCatalogs.add(SpringTemplateCatalog.WEBFLUX_CATALOG);
            } else {
                if (restEndpointDescriptor.isWithMongoDb()) {
                    appliedCatalogs.add(SpringTemplateCatalog.WEBMVC_MONGODB_CATALOG);
                } else {
                    appliedCatalogs.add(SpringTemplateCatalog.WEBMVC_CATALOG);
                }
            }
        } else {
            log.debug("[beforeCollection] descriptor is-a {}", descriptor.getClass().getName());
        }
        return this;
    }

    /**
     * This collects both SpringWebMvc and SpringWebFlux
     * endpoint templates, so a Predicate is needed to
     * filter the templates applicable to the framework being used.
     *
     * @return a collection containing "endpoint" templates
     * (and others, so be mindful to apply a Predicate to filter
     * for the desired templates).
     */
    @Override
    public List<CatalogEntry> collect() {
        List<CatalogEntry> resultSet = new ArrayList<>();
        for (String catalog : appliedCatalogs) {
            Optional<TemplateCatalog> optional = reader.readCatalog(catalog);
            optional.ifPresent(tc -> resultSet.addAll(tc.getEntries()));
        }
        return resultSet;
    }
}
