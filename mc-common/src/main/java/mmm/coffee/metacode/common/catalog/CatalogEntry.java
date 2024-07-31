/*
 * Copyright 2022-2024 Jon Caulfield
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
package mmm.coffee.metacode.common.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;
import mmm.coffee.metacode.common.model.Archetype;

import java.util.ArrayList;
import java.util.List;

/**
 * CatalogEntry
 * <p>
 * See: <a href="https://howtodoinjava.com/jackson/ignore-null-empty-absent-values/">Ignoring values</a>
 * for details on how the `Include` annotation works.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
// The code doesn't care what order the properties are in,
// but this order is easier to read.
@JsonPropertyOrder({"scope", "archetype", "facets"})
@ToString
public class CatalogEntry {
    /*
     * This `scope` tells us if a template is applied to a project or endpoint.
     * The scope can only be `project` or `endpoint`
     */
    private String scope;

    /*
     * The `archetype` references the "meta-class", as it were, that can be queried
     * to determine things like:
     *  - the simple classname
     *  - the fully-qualified classname
     *  - the package name
     *  - the canonical filename of the file emitted by the template (eg, application/src/main/java/org/example/Application.java)
     */
    @JsonProperty("archetype")
    private String archetype; // eg: Controller, Service, etc.

    public Archetype archetypeValue() {
        if (archetype == null)
            return Archetype.Undefined;
        return Archetype.valueOf(this.archetype);
    }

    /*
     * The `facet` tells us, for a given template, whether the template (and emitted file)
     * are for the "main", "test" or "integrationTest" facets.
     */
    private List<TemplateFacet> facets = new ArrayList<>();

    /*
     * A comma-separated list of tags applied to a template; such as `postgres` or `mongodb`
     * These tags act as qualifiers (or only-when conditions) to indicate when the template is used.
     * For example, when the tag `postgres` is present, then postgres-specific templates
     * are included.
     */
    private String tags;
}
