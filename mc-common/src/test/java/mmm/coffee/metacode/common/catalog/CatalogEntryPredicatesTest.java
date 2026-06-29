package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogEntryPredicatesTest {

    @Test
    void shouldRecognizeCommonProjectArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isCommonProjectArtifact();

        assertThat(predicate.test(aBasicProjectCatalogEntry())).isTrue();
        assertThat(predicate.test(aProjectCatalogEntryWithEmptyTags())).isTrue();

        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isFalse();
        assertThat(predicate.test(aCatalogEntryWithNullScope())).isFalse();
    }

    @Test
    void shouldRecognizeEndpointArtifact() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isEndpointArtifact();

        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isTrue();

        assertThat(predicate.test(aBasicProjectCatalogEntry())).isFalse();
        assertThat(predicate.test(aCatalogEntryWithNullScope())).isFalse();
    }

    @Test
    void shouldRecognizeWebFluxArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isWebFluxArtifact();

        assertThat(predicate.test(aWebFluxCatalogEntry())).isTrue();

        assertThat(predicate.test(aWebMvcCatalogEntry())).isFalse();
        assertThat(predicate.test(aCatalogEntryWithEmptyFacets())).isFalse();
    }

    @Test
    void shouldRecognizeWebMvcArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isWebMvcArtifact();

        assertThat(predicate.test(aWebMvcCatalogEntry())).isTrue();

        assertThat(predicate.test(aWebFluxCatalogEntry())).isFalse();
        assertThat(predicate.test(aCatalogEntryWithEmptyFacets())).isFalse();
    }

    @Test
    void shouldAcceptWhenHasPostgresTag() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.hasPostgresTag();

        assertThat(predicate.test(aPostgresCatalogEntry())).isTrue();
        assertThat(predicate.test(aMongoDbCatalogEntry())).isFalse();
    }

    @Test
    void shouldAcceptWhenHasTestContainersTag() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.hasTestContainerTag();
        assertThat(predicate.test(aTestContainersCatalogEntry())).isTrue();
    }

    @Test
    void shouldRejectWhenMissingTestContainersTag() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.hasTestContainerTag();
        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isFalse();
    }

    @Test
    void shouldRecognizeLiquibaseTags() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.hasLiquibaseTag();
        assertThat(predicate.test(aLiquibaseCatalogEntry())).isTrue();
    }

    @Test
    void shouldRecognizeFlywayTags() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isProjectScopeFlywayArtifact();
        assertThat(predicate.test(aFlywayCatalogEntry())).isTrue();
    }

    @Test
    void isProjectScopeFlywayArtifact_coversAllConditions() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isProjectScopeFlywayArtifact();

        // condition 1: scope == null -> false
        assertThat(predicate.test(aCatalogEntryWithNullScope())).isFalse();

        // condition 2: scope != null but not "project" -> false
        assertThat(predicate.test(aFlywayEndpointCatalogEntry())).isFalse();

        // condition 3: scope == "project" but no flyway tag -> false
        assertThat(predicate.test(aBasicProjectCatalogEntry())).isFalse();

        // all conditions true: scope == "project" and has flyway tag -> true
        assertThat(predicate.test(aFlywayCatalogEntry())).isTrue();
    }

    @Test
    void shouldRecognizeFlywayTagsOnEndpointEntry() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isEndpointScopeFlywayArtifact();
        assertThat(predicate.test(aFlywayEndpointCatalogEntry())).isTrue();
    }

    @Test
    void isEndpointScopeFlywayArtifact_coversAllConditions() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isEndpointScopeFlywayArtifact();

        // condition 1: scope == null -> false
        assertThat(predicate.test(aCatalogEntryWithNullScope())).isFalse();

        // condition 2: scope != null but not "endpoint" -> false
        assertThat(predicate.test(aFlywayCatalogEntry())).isFalse();

        // condition 3: scope == "endpoint" but no flyway tag -> false
        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isFalse();

        // all conditions true: scope == "endpoint" and has flyway tag -> true
        assertThat(predicate.test(aFlywayEndpointCatalogEntry())).isTrue();
    }

    @Test
    void shouldRecognizeKafkaTags() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.hasKafkaTag();
        assertThat(predicate.test(aKafkaCatalogEntry())).isTrue();
    }

    @Test
    void isCommonProjectArtifact_coversAllTagsConditions() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isCommonProjectArtifact();

        // condition 1: tags == null -> true
        assertThat(predicate.test(aBasicProjectCatalogEntry())).isTrue();

        // condition 2: tags.isBlank() -> true (empty string)
        assertThat(predicate.test(aProjectCatalogEntryWithEmptyTags())).isTrue();

        // condition 3: tags non-null and non-blank -> false (project scope but has tags; not a common artifact)
        assertThat(predicate.test(aPostgresCatalogEntry())).isFalse();
    }

    @Test
    void whenArchetypeIsNull_shouldReturnUndefined() {
        CatalogEntry entry = new CatalogEntry();
        entry.setArchetype(null);
        assertThat(entry.archetypeValue()).isEqualTo(Archetype.Undefined);

    }


    /* ------------------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------------------------------ */

    private CatalogEntry aBasicProjectCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aCatalogEntryWithNullScope() {
        return CatalogEntryBuilder.builder()
                .scope(null)
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aProjectCatalogEntryWithEmptyTags() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .tags("")
                .build();
    }


    private CatalogEntry aBasicEndpointCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("endpoint")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aWebFluxCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .addFacet(aWebFluxTemplate())
                .build();
    }

    private CatalogEntry aWebMvcCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .addFacet(aWebMvcTemplate())
                .build();
    }

    private CatalogEntry aPostgresCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aMongoDbCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("mongodb")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aCatalogEntryWithEmptyFacets() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .build();
    }

    private CatalogEntry aTestContainersCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("mongodb,testcontainers")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aLiquibaseCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres,liquibase")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aFlywayCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres,flyway")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }

    private CatalogEntry aFlywayEndpointCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("endpoint")
                .tags("flyway")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }


    private CatalogEntry aKafkaCatalogEntry() {
        return CatalogEntryBuilder.builder()
                .scope("project")
                .tags("kafka")
                .addFacet(aMainComponentTemplate())
                .addFacet(aVanillaTemplate())
                .build();
    }


    private TemplateFacet aMainComponentTemplate() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("main");
        facet.setSourceTemplate("/some/main/template.ftl");
        facet.setDestination("/path/to/main/example.properties");
        return facet;
    }

    private TemplateFacet aVanillaTemplate() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("test");
        facet.setSourceTemplate("/some/test/template.ftl");
        facet.setDestination("/path/to/test/example.properties");
        return facet;
    }

    private TemplateFacet aWebFluxTemplate() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("main");
        facet.setSourceTemplate("/some/main/webflux/template.ftl");
        return facet;
    }

    private TemplateFacet aWebMvcTemplate() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("main");
        facet.setSourceTemplate("/some/main/webmvc/template.ftl");
        return facet;
    }
}
