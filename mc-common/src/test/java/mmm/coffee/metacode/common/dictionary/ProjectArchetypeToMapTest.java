package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectArchetypeToMapTest {


    @Test
    void shouldContainProjectArchetypes() {
        assertThat(ProjectArchetypeToMap.PROJECT_ARCHETYPES).hasSizeGreaterThan(1);
    }

    @Test
    void shouldReturnMapOfArchetypeDescriptors() {
        Map<String, ArchetypeDescriptor> map = ProjectArchetypeToMap.map(anArchetypeDescriptorFactory());

        assertThat(map).isNotEmpty();
        // The next to entries have explicit package namespaces assigned in the example layout rules
        assertThat(map.get(Archetype.Application.toString())).isNotNull();
        assertThat(map.get(Archetype.ResourceNotFoundException.toString())).isNotNull();

        // when the package-layout does not declare a package namespace, a default should be assigned
        assertThat(map.get(Archetype.GlobalExceptionHandler.toString())).isNotNull();
        // the following classes are webflux-specific
        assertThat(map.get(Archetype.GlobalErrorWebExceptionHandler.toString())).isNotNull();
        assertThat(map.get(Archetype.GlobalErrorAttributes.toString())).isNotNull();


    }

    @Test
    void shouldThrowExceptionIfFactoryIsNull() {
        assertThrows(NullPointerException.class, () -> { ProjectArchetypeToMap.map(null); });
    }
    /* ------------------------------------------------------------------------------------------------
     * HELPER METHODS
     * ------------------------------------------------------------------------------------------------ */

    IArchetypeDescriptorFactory anArchetypeDescriptorFactory() {
        return new ArchetypeDescriptorFactory(packageLayoutRuleSet(),
                classNameRuleSet());
    }

    PackageLayoutRuleSet packageLayoutRuleSet() {
        // One rule is sufficient for this test
        return new PackageLayoutRuleSet(Map.of(
                "org.example", Archetype.Application.toString(),
                "org.example.exception", Archetype.ResourceNotFoundException.toString())
        );
    }
    ClassNameRuleSet classNameRuleSet() {
        // A couple of classname rules are sufficient
        return new ClassNameRuleSet(Map.of(
                Archetype.Application.toString(), "Application",
                Archetype.Controller.toString(), "PetController"));
    }
}
