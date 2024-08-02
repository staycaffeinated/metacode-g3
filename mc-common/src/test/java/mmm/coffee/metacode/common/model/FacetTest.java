package mmm.coffee.metacode.common.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FacetTest {

    @Test
    void shouldReturnUndefinedForUnknownString() {
        Facet actual = Facet.fromString("SillyCowboyTrucksAreForGirls");
        assertThat(actual).isEqualTo(Facet.Undefined);
    }

    @Test
    void shouldReturnCorrectFacetForKnownString() {
        Facet actual = Facet.fromString(Facet.Main.name());
        assertThat(actual).isEqualTo(Facet.Main);
    }
}
