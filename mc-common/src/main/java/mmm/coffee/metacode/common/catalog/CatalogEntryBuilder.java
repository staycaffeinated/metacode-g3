package mmm.coffee.metacode.common.catalog;

import java.util.ArrayList;
import java.util.List;

public class CatalogEntryBuilder {

    private String scope;
    private Archetype archetype;
    private List<TemplateFacet> facets = new ArrayList<>();
    private String tags;

    public static CatalogEntryBuilder builder() {
        return new CatalogEntryBuilder();
    }

    public CatalogEntryBuilder scope(String scope) {
        this.scope = scope;
        return this;
    }

    public CatalogEntryBuilder archetype(Archetype archetype) {
        this.archetype = archetype;
        return this;
    }

    public CatalogEntryBuilder facets(List<TemplateFacet> facets) {
        this.facets = facets;
        return this;
    }

    public CatalogEntryBuilder addFacet(TemplateFacet facet) {
        facets.add(facet);
        return this;
    }

    public CatalogEntryBuilder tags(String tags) {
        this.tags = tags;
        return this;
    }

    public CatalogEntry build() {
        CatalogEntry catalogEntry = new CatalogEntry();
        catalogEntry.setScope(scope);
        catalogEntry.setArchetype(archetype);
        catalogEntry.setFacets(facets);
        catalogEntry.setTags(tags);
        return catalogEntry;
    }
}
