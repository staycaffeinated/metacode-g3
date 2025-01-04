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
package mmm.coffee.metacode.common.catalog;

import com.google.common.base.Predicate;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Predicates applicable to collections of CatalogEntry instances
 */
@SuppressWarnings("java:S4738") // S4738: need to refactor to Java8 predicates; for now, leave it
public class CatalogEntryPredicates {

    // hidden constructor
    private CatalogEntryPredicates() {
    }

    /**
     * Returns {@code true} if the CatalogEntry is for a project artifact
     * and contains no tags
     */
    public static Predicate<CatalogEntry> isCommonProjectArtifact() {
        return p -> p.getScope() != null && p.getScope().contains("project")
                && (p.getTags() == null || p.getTags().isBlank());
    }

    /**
     * Returns {@code true} if the CatalogEntry is for an endpoint artifact
     */
    public static Predicate<CatalogEntry> isEndpointArtifact() {
        return p -> p.getScope() != null && p.getScope().contains("endpoint");
    }

    /**
     * Returns {@code true} if the CatalogEntry is for a webflux artifact
     */
    public static Predicate<CatalogEntry> isWebFluxArtifact() {
        return CatalogEntryPredicates::isWebFluxTemplate;
    }

    /**
     * Returns {@code true} if the CatalogEntry is for a webmvc artifact
     */
    public static Predicate<CatalogEntry> isWebMvcArtifact() {
        return CatalogEntryPredicates::isWebMvcTemplate;
    }

    /**
     * Returns {@code true} if the CatalogEntry's tags includes {@code postgres}
     */
    public static Predicate<CatalogEntry> hasPostgresTag() {
        return p -> containsTag(p, "postgres");
    }

    /**
     * Returns {@code true} if the CatalogEntry's tags includes {@code postgres}
     */
    public static Predicate<CatalogEntry> hasTestContainerTag() {
        return p -> containsTag(p, "testcontainer");
    }

    /**
     * Returns {@code true} if the CatalogEntry's tags includes {@code liquibase}
     */
    public static Predicate<CatalogEntry> hasLiquibaseTag() {
        return p -> containsTag(p, "liquibase");
    }

    /**
     * Returns {@code true} if the CatalogEntry's tag includes {@code kafka}
     */
    public static Predicate<CatalogEntry> hasKafkaTag() {
        return p -> containsTag(p, "kafka");
    }

    private static boolean containsTag(CatalogEntry entry, String text) {
        return entry.getTags() != null && entry.getTags().contains(text);
    }

    @SuppressWarnings("java:S1135") // don't nag about to do items
    /*
     * TODO: Find something more reliable than using the path to determine
     *  the intent of the template.
     */
    private static boolean isWebFluxTemplate(CatalogEntry entry) {
        if (!entry.getFacets().isEmpty()) {
            return entry.getFacets().get(0).getSourceTemplate().contains("/webflux/");
        }
        return false;
    }

    private static boolean isWebMvcTemplate(CatalogEntry entry) {
        if (!entry.getFacets().isEmpty()) {
            return entry.getFacets().get(0).getSourceTemplate().contains("/webmvc/");
        }
        return false;
    }
}
