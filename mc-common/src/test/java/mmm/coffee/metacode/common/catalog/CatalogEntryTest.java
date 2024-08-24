package mmm.coffee.metacode.common.catalog;

import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.Facet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogEntryTest {
    CatalogEntry catalogEntryUnderTest = new CatalogEntry();

    private static final String EXPECTED_SCOPE = "endpoint";
    private static final String EXPECTED_ARCHETYPE = Archetype.Controller.toString();

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

    @Test
    void shouldReturnUndefinedIfArchetypeIsUnset() {
        CatalogEntry entry = new CatalogEntry();
        assertThat(entry.archetypeValue()).isEqualTo(Archetype.Undefined);
    }

    @Test
    void verifyGetters() {
        CatalogEntry entry = CatalogEntryBuilder.builder()
                .scope(EXPECTED_SCOPE)
                .addFacet(TemplateFacetBuilder.builder()
                        .source("/src/main/template.ftl")
                        .destination("/output.txt")
                        .facet(Facet.Main.name())
                        .build())
                .addFacet(TemplateFacetBuilder.builder()
                        .source("src/test/template.ftl")
                        .destination("/test-output.txt")
                        .facet(Facet.Test.name()).build())
                .archetype(Archetype.Application.name())
                .build();

        assertThat(entry.getArchetype()).isEqualTo(Archetype.Application.name());
        assertThat(entry.getFacets()).hasSize(2);
        assertThat(entry.getScope()).isEqualTo(EXPECTED_SCOPE);
        assertThat(entry.getTags()).isBlank();
    }

    @Test
    void verifySetters() {
        CatalogEntry entry = new CatalogEntry();
        entry.setScope(EXPECTED_SCOPE);
        entry.setArchetype(Archetype.Application.name());
        entry.setFacets(exampleFacets());
        entry.setTags("postgres");

        assertThat(entry.getScope()).isEqualTo(EXPECTED_SCOPE);
        assertThat(entry.getArchetype()).isEqualTo(Archetype.Application.name());
        assertThat(entry.getFacets()).hasSameSizeAs(exampleFacets());
        assertThat(entry.getTags()).isEqualTo("postgres");
    }

    @Test
    void verifyDefaultArchetypeIsSetWhenUnknownOrNull() {
        CatalogEntry entry = new CatalogEntry();
        entry.setArchetype(null);
        assertThat(entry.getArchetype()).isNotNull().isEqualTo(Archetype.Undefined.name());
    }

    @Test
    void shouldReturnArchetypeValueThatMatchesArchetype() {
        CatalogEntry entry = new CatalogEntry();
        entry.setArchetype(Archetype.Text.name());
        assertThat(entry.archetypeValue()).isEqualTo(Archetype.Text);
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
