package mmm.coffee.metacode.common.catalog;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateFacetBuilderTest {

    @Test
    void shouldBuildFacet() {
        String srcMain = "main";
        String sourcePath = "/some/template.ftl";
        String someTarget = "example.properties";


        TemplateFacet facet = TemplateFacetBuilder.builder()
                .facet(srcMain)
                .source(sourcePath)
                .destination(someTarget)
                .build();

        assertThat(facet.getFacet()).isEqualTo(srcMain);
        assertThat(facet.getSourceTemplate()).isEqualTo(sourcePath);
        assertThat(facet.getDestination()).isEqualTo(someTarget);

    }

}
