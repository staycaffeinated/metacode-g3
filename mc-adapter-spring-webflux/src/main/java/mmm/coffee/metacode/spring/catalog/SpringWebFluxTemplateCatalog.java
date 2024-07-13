/*
 * Copyright 2022 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.spring.catalog;

import lombok.extern.slf4j.Slf4j;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import mmm.coffee.metacode.common.descriptor.Descriptor;
import mmm.coffee.metacode.common.stereotype.Collector;

import java.util.List;

/**
 * Reads the catalogs of Spring WebFlux templates
 */
@Slf4j
public class SpringWebFluxTemplateCatalog extends SpringTemplateCatalog {

    private String activeCatalog;


    /**
     * Constructor
     *
     * @param reader the CatalogReader
     */
    public SpringWebFluxTemplateCatalog(ICatalogReader reader) {
        super(reader);
    }

    /**
     * Returns the catalog selected for collection
     * This is exposed to make the state available for unit tests
     */
    String getActiveCatalog() {
        if (activeCatalog == null) {
            return WEBFLUX_CATALOG;
        }
        return activeCatalog;
    }

    public Collector prepare(Descriptor descriptor) {
        log.debug("[prepare] starting 'prepare' step of life-cycle...");

        // We do not yet support WebFlux + MongoDB. When we do, this method needs to be updated.
        // In the meantime, only one catalog is available.
        activeCatalog = WEBFLUX_CATALOG;
        return this;
    }

    @Override
    public List<CatalogEntry> collect() {
        return super.collectGeneralCatalogsAndThisOne(getActiveCatalog());
    }
}
