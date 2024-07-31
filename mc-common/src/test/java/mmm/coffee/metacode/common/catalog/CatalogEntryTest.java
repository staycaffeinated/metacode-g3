package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogEntryTest {
    CatalogEntry catalogEntryUnderTest = new CatalogEntry();

    private static final String EXPECTED_SCOPE = "endpoint";
    private static final String EXPECTED_ARCHETYPE =  Archetype.Controller.toString();

    @BeforeEach
    public void setUp() {
        catalogEntryUnderTest.setArchetype(EXPECTED_ARCHETYPE);
        catalogEntryUnderTest.setScope(EXPECTED_SCOPE);
        catalogEntryUnderTest.setFacets(exampleFacets());
    }

    @Test
    void shouldReturnScope() {
        assertThat(catalogEntryUnderTest.getScope()).isEqualTo(EXPECTED_SCOPE);
    }

    @Test
    void shouldReturnArchetype() {
        assertThat(catalogEntryUnderTest.getArchetype()).isEqualTo(EXPECTED_ARCHETYPE);
    }

    @Test
    void shouldReturnArchetypeValue() {
        assertThat(catalogEntryUnderTest.archetypeValue()).isEqualTo(Archetype.Controller);
    }

    @Test
    void shouldReturnFacets() {
        assertThat(catalogEntryUnderTest.getFacets()).isEqualTo(exampleFacets());
    }

    @Test
    void shouldSupportToString() {
        assertThat(catalogEntryUnderTest.toString()).isNotBlank();
    }

    /* ----------------------------------------------------------------
     * HELPER METHODS
     * ---------------------------------------------------------------- */
    List<TemplateFacet> exampleFacets() {
        return List.of(sampleFacet("main"),
                sampleFacet("test"),
                sampleFacet("integrationTest"));
    }

    TemplateFacet sampleFacet(String facet) {
        TemplateFacet templateFacet = new TemplateFacet();
        templateFacet.setFacet(facet);
        templateFacet.setSourceTemplate("/path/to/some/template.ftl");
        return templateFacet;
    }
}
