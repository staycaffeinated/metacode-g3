package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackageLayoutRuleSetTest {
    PackageLayoutRuleSet rulesUnderTest;

    @BeforeEach
    public void initRuleSet() {
        HashMap<String, String> rules = new HashMap<>();
        Archetype[] values = Archetype.values();
        for (Archetype e : values) {
            rules.put(e.toString(), "com.acme.petstore");
        }
        rules.put("Controller", "{{basePackage}}.{{restResource}}.api");
        rules.put("ServiceApi", "{{basePackage}}.{{restResource}}.api");
        rules.put("ServiceImpl", "{{basePackage}}.{{restResource}}.api");
        rules.put("Routes", "{{basePackage}}.{{restResource}}.api");
        rules.put("Application", "com.acme.petstore");
        rules.put("GlobalExceptionHandler", "{{basePackage}}.error");

        rulesUnderTest = new PackageLayoutRuleSet(rules);
    }

    @Test
    void shouldResolveProjectScopeArtifact() {
        String pkg = rulesUnderTest.resolvePackageName("Application");
        assertThat(pkg).isEqualTo("com.acme.petstore");

        String pkg2 = rulesUnderTest.resolvePackageName("GlobalExceptionHandler");
        assertThat(pkg2).isEqualTo("{{basePackage}}.error");
    }

    @Test
    void shouldResolveEndpointScopeArtifact() {
        String pkg = rulesUnderTest.resolvePackageName("Controller", "Pet");
        assertThat(pkg).isEqualTo("{{basePackage}}.pet.api");

        String storePkg = rulesUnderTest.resolvePackageName("Controller", "Store");
        assertThat(storePkg).isEqualTo("{{basePackage}}.store.api");
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionIfRulesAreNull(Map<String,String> rules) {
        assertThrows(NullPointerException.class, () -> new PackageLayoutRuleSet(rules));
    }

    @Test
    void shouldReturnDefaultForUnmappedArchetype() {
        String pkgName = rulesUnderTest.resolvePackageName(Archetype.PersistenceAdapter);
        assertThat(pkgName).isEqualTo("com.acme.petstore");
    }
}
