package mmm.coffee.metacode.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;

@SuppressWarnings({
        "java:S115" // enum values do not need to be in all-cap's
})
public enum Facet {
    Main("main"),
    Test("test"),
    IntegrationTest("integrationTest"),
    TestFixtures("testFixtures"),

    // for unknown values
    Undefined("Undefined");

    private final String stringValue;

    // sealed
    Facet(String name) {
        this.stringValue = name;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Enables Jackson library to handle undefined values gracefully.
     * Unknown values will be mapped to the `Unknown` value.
     *
     * @param value the text being mapped to a Facet
     * @return the corresponding Facet of `value`
     */
    @JsonCreator
    public static Facet fromString(String value) {
        try {
            return Facet.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Facet.Undefined;
        }
    }


}
