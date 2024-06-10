package mmm.coffee.metacode.common.dictionary.functions;

import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class PackageLayoutRuleSetTest {
    PackageLayoutRuleSet rulesUnderTest;

    @BeforeEach
    public void initRuleSet() {
        HashMap<String, String> rules = new HashMap<>();
        Archetype[] values = Archetype.values();
        for (Archetype e : values) {
            rules.put(e.toString(), "com.acme.petstore");
        }
        rules.put("Controller", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceApi", "com.acme.petstore.{{restResource}}.api");
        rules.put("ServiceImpl", "com.acme.petstore.{{restResource}}.api");
        rules.put("Routes", "com.acme.petstore.{{restResource}}.api");
        rules.put("Application", "com.acme.petstore");

        rulesUnderTest = new PackageLayoutRuleSet(rules);
    }

    @Test
    void shouldResolveProjectScopeArtifact() {
        String pkg = rulesUnderTest.resolvePackageName("Application");
        assertThat(pkg).isEqualTo("com.acme.petstore");
    }

    @Test
    void shouldResolveEndpointScopeArtifact() {
        String pkg = rulesUnderTest.resolvePackageName("Controller", "Pet");
        assertThat(pkg).isEqualTo("com.acme.petstore.pet.api");

        String storePkg = rulesUnderTest.resolvePackageName("Controller", "Store");
        assertThat(storePkg).isEqualTo("com.acme.petstore.store.api");
    }
}
