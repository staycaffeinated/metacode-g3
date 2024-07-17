/*
 * Copyright 2020-2024 Jon Caulfield
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

import lombok.NonNull;
import mmm.coffee.metacode.annotations.guice.SpringWebFlux;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.catalog.ICatalogReader;
import mmm.coffee.metacode.common.catalog.TemplateCatalog;
import mmm.coffee.metacode.common.exception.RuntimeApplicationError;
import mmm.coffee.metacode.common.stereotype.Collector;

import java.io.IOException;
import java.util.*;


/**
 * Base class for loading the catalog entries
 */
@SuppressWarnings({"java:S125"})
// S125: we're OK with comments that look like code
public abstract class SpringTemplateCatalog implements Collector {

    public static final String WEBFLUX_CATALOG = "/spring/catalogs/v3/spring-webflux-v3.yml";

    public static final String WEBMVC_CATALOG = "/spring/catalogs/v3/spring-webmvc-v3.yml";
    public static final String WEBMVC_MONGODB_CATALOG = "/spring/catalogs/v3/spring-webmvc-mongodb-v3.yml";


    private static final String[] COMMON_CATALOGS = {
            "/spring/catalogs/v2/common-stuff.yml",
            "/spring/catalogs/v2/spring-gradle.yml"
    };

    final ICatalogReader reader;

    /*
     * Most likely, reader is-a CatalogFileReader
     */
    protected SpringTemplateCatalog(@NonNull ICatalogReader reader) {
        this.reader = reader;
    }

    /**
     * Reads the general catalog files and the {@code specificCatalog}
     *
     * @param specificCatalog a specific file to include
     * @return the collection of CatalogEntry's
     */
    protected List<CatalogEntry> collectGeneralCatalogsAndThisOne(@NonNull String specificCatalog) {
        Set<CatalogEntry> resultSet = new HashSet<>();

        for (String catalog : COMMON_CATALOGS) {
            try {
                TemplateCatalog templateCatalog = reader.readCatalog(catalog);
                if (templateCatalog != null) {
                    resultSet.addAll(templateCatalog.getEntries());
                }
                resultSet.addAll(reader.readCatalog(specificCatalog).getEntries());
            } catch (IOException e) {
                throw new RuntimeApplicationError("An error occurred while reading the Spring template catalogs", e);
            }
        }
        return resultSet.stream().toList();
    }

    public Set<String> catalogs() {
        Set<String> candidates = new TreeSet<>();
        // Doing catalogs.stream().toList() is CPU expensive.
        for (String commonCatalog : COMMON_CATALOGS) {
            candidates.add(commonCatalog);
        }
        candidates.add(WEBMVC_CATALOG);
        candidates.add(WEBMVC_MONGODB_CATALOG);
        candidates.add(WEBFLUX_CATALOG);
        return candidates;
    }
}
