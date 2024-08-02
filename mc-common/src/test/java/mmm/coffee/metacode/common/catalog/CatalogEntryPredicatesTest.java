package mmm.coffee.metacode.common.catalog;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogEntryPredicatesTest {

    @Test
    void shouldRecognizeCommonProjectArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isCommonProjectArtifact();

        assertThat(predicate.test(aBasicProjectCatalogEntry())).isTrue();
        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isFalse();
    }

    @Test
    void shouldRecognizeEndpointArtifact() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isEndpointArtifact();

        assertThat(predicate.test(aBasicEndpointCatalogEntry())).isTrue();
        assertThat(predicate.test(aBasicProjectCatalogEntry())).isFalse();
    }

    @Test
    void shouldRecognizeWebFluxArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isWebFluxArtifact();

        assertThat(predicate.test(aWebFluxCatalogEntry())).isTrue();
        assertThat(predicate.test(aWebMvcCatalogEntry())).isFalse();
    }

    @Test
    void shouldRecognizeWebMvcArtifacts() {
        Predicate<CatalogEntry> predicate = CatalogEntryPredicates.isWebMvcArtifact();
        
        assertThat(predicate.test(aWebMvcCatalogEntry())).isTrue();
        assertThat(predicate.test(aWebFluxCatalogEntry())).isFalse();
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
