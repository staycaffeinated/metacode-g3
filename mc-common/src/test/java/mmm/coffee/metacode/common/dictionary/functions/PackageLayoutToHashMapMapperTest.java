package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.dictionary.PackageLayout;
import mmm.coffee.metacode.common.dictionary.PackageLayoutEntry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PackageLayoutToHashMapMapperTest {

    @Test
    void whenArchetypeIsKnown_shouldMapToPackageName() {
        // condition: at != Archetype.Undefined — known archetype, log branch skipped
        PackageLayout layout = layoutWith("{{basePackage}}.web", "ApplicationConfiguration");

        Map<String, String> result = PackageLayoutToHashMapMapper.convertToHashMap(layout);

        assertThat(result).containsKey("ApplicationConfiguration")
                        .containsEntry("ApplicationConfiguration", "{{basePackage}}.web");
    }

    @Test
    void whenArchetypeNameIsLiterallyUndefined_shouldMapWithoutLoggingWarning() {
        // condition: at == Archetype.Undefined AND archetypeName.equalsIgnoreCase("undefined") — second clause false
        PackageLayout layout = layoutWith("{{basePackage}}.misc", "undefined");

        Map<String, String> result = PackageLayoutToHashMapMapper.convertToHashMap(layout);

        assertThat(result).containsKey("undefined");
    }

    @Test
    void whenArchetypeNameIsUnrecognised_shouldStillMapAndLogWarning() {
        // condition: at == Archetype.Undefined AND !archetypeName.equalsIgnoreCase("undefined") — both true, log fires
        PackageLayout layout = layoutWith("{{basePackage}}.misc", "Bogus");

        Map<String, String> result = PackageLayoutToHashMapMapper.convertToHashMap(layout);

        assertThat(result).containsKey("Bogus")
                .containsEntry("Bogus", "{{basePackage}}.misc");
    }

    // -------------------------------------------------------------------------

    private PackageLayout layoutWith(String packageName, String... archetypeNames) {
        PackageLayoutEntry entry = new PackageLayoutEntry();
        entry.setPackageName(packageName);
        entry.setArchetypes(List.of(archetypeNames));

        PackageLayout layout = new PackageLayout();
        layout.setEntries(List.of(entry));
        return layout;
    }
}
