package mmm.coffee.metacode.common.toml;

import mmm.coffee.metacode.common.model.Archetype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class DefaultPackageDataDictionaryTest {

    private final String basePackage = "org.example.petstore";

    DefaultPackageDataDictionary classUnderTest = new DefaultPackageDataDictionary();

    @BeforeEach
    public void setUp() {
        classUnderTest.setBasePackage(basePackage);
        initDictionary();
    }


    @Test
    void shouldReturnBasePackageName() {
        assertThat(classUnderTest.basePackage()).isEqualTo(basePackage);
    }

    @Test
    void shouldReturnSomething() {
        classUnderTest.add(Archetype.ServiceImpl, "pet.impl");
        String actualPkgName = classUnderTest.packageName(Archetype.ServiceImpl);
        assertThat(actualPkgName).isNotEmpty();
        assertThat(actualPkgName).isEqualTo(basePackage + ".pet.impl");
    }

    private void initDictionary() {
        // Add a TestFixture for this stuff to make it easier
        Map<Archetype, String> items = new EnumMap<>(Archetype.class);
        items.put(Archetype.AlphabeticAnnotation, "infra.validation");
        items.put(Archetype.AlphabeticValidator, "infra.validation");
        items.put(Archetype.ProblemConfiguration, "infra.config");
        items.put(Archetype.ResourcePojo, "domain");
        items.put(Archetype.Routes, "api");
        items.put(Archetype.ServiceApi, "api");
        items.put(Archetype.ServiceImpl, "api.impl");
        items.put(Archetype.Controller, "api");
        classUnderTest.addAll(items);
    }

}
