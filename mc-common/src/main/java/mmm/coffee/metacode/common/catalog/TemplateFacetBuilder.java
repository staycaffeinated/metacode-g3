package mmm.coffee.metacode.common.catalog;

public class TemplateFacetBuilder {
    private String facet;
    private String sourceTemplate;
    private String destination;

    public static TemplateFacetBuilder builder() {
        return new TemplateFacetBuilder();
    }

    public TemplateFacetBuilder facet(String facet) {
        this.facet = facet;
        return this;
    }
    public TemplateFacetBuilder source(String sourceTemplate) {
        this.sourceTemplate = sourceTemplate;
        return this;
    }
    public TemplateFacetBuilder destination(String destination) {
        this.destination = destination;
        return this;
    }
    public TemplateFacet build() {
        TemplateFacet templateFacet = new TemplateFacet();
        templateFacet.setFacet(facet);
        templateFacet.setSourceTemplate(sourceTemplate);
        templateFacet.setDestination(destination);
        return templateFacet;
    }
}
