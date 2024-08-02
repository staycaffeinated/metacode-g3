package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.common.model.Archetype;
import org.apache.commons.lang3.arch.Processor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogEntryBuilderTest {

    @Test
    void shouldBuildCatalogEntryFromIndividualFacets() {
        CatalogEntry entry = CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres")
                .addFacet(mainFacet())
                .archetype(Archetype.Application.toString())
                .addFacet(testFacet())
                .build();

        assertThat(entry.getArchetype()).isEqualTo(Archetype.Application.toString());
        assertThat(entry.getFacets()).hasSize(2);
        assertThat(entry.getScope()).isEqualTo("project");
        assertThat(entry.getTags()).isEqualTo("postgres");
    }

    @Test
    void shouldBuildCatalogEntryFromListOfFacets() {
        List<TemplateFacet> facets = List.of(mainFacet(), testFacet());

        CatalogEntry entry = CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres")
                .facets(facets)
                .archetype(Archetype.Application.toString())
                .build();

        assertThat(entry.getArchetype()).isEqualTo(Archetype.Application.toString());
        assertThat(entry.getFacets()).hasSize(2);
        assertThat(entry.getScope()).isEqualTo("project");
        assertThat(entry.getTags()).isEqualTo("postgres");
    }

    @Test
    void shouldBuildCatalogEntryFromListOfFacetsAndIndividualFacet() {
        List<TemplateFacet> facets = new ArrayList<>();
        facets.add(mainFacet());

        CatalogEntry entry = CatalogEntryBuilder.builder()
                .scope("project")
                .tags("postgres")
                .facets(facets)
                .addFacet(testFacet())
                .archetype(Archetype.Application.toString())
                .build();

        assertThat(entry.getArchetype()).isEqualTo(Archetype.Application.toString());
        assertThat(entry.getFacets()).hasSize(2);
        assertThat(entry.getScope()).isEqualTo("project");
        assertThat(entry.getTags()).isEqualTo("postgres");
    }

    /* ---------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * --------------------------------------------------------------------------------------------------- */

    private TemplateFacet mainFacet() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("main");
        facet.setSourceTemplate("/some/main/template.ftl");
        facet.setDestination("/path/to/main/example.properties");
        return facet;
    }

    private TemplateFacet testFacet() {
        TemplateFacet facet = new TemplateFacet();
        facet.setFacet("test");
        facet.setSourceTemplate("/some/test/template.ftl");
        facet.setDestination("/path/to/test/example.properties");
        return facet;
    }

}
