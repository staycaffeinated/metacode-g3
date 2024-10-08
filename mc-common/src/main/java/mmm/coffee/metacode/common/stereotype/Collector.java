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
package mmm.coffee.metacode.common.stereotype;

import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;
import mmm.coffee.metacode.common.catalog.CatalogEntry;
import mmm.coffee.metacode.common.descriptor.Descriptor;
import mmm.coffee.metacode.common.trait.CollectTrait;

import java.util.Set;

/**
 * Stereotype for Collectors of CatalogEntries
 */
@ExcludeFromJacocoGeneratedReport
public interface Collector extends CollectTrait<CatalogEntry> {
    /**
     * Invoke this method to determine which catalogs to collect,
     * based on the {@code descriptor}.  The default behavior is
     * to do nothing here, allowing the default catalog to be loaded.
     */
    default Collector prepare(Descriptor descriptor) {
        return this;
    }

    default Set<String> catalogs() {
        return Set.of();
    }
}
