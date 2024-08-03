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

import java.util.List;

/**
 * Reads the catalogs of Spring WebFlux templates
 */
@Slf4j
@SuppressWarnings({
        "java:S115" // allow static var names to use lower-case
})
public class SpringWebFluxTemplateCatalog extends SpringTemplateCatalog {

    // We do not yet support WebFlux + MongoDB. When we do, this needs to be refactored.
    // In the meantime, since only one catalog is available...
    private static final String activeCatalog = WEBFLUX_CATALOG;

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
        return activeCatalog;
    }

    @Override
    public List<CatalogEntry> collect() {
        return super.collectGeneralCatalogsAndThisOne(getActiveCatalog());
    }
}
