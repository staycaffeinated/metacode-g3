package mmm.coffee.metacode.common.toml;

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
        classUnderTest.add(PrototypeClass.ServiceImpl, "pet.impl");
        String actualPkgName = classUnderTest.packageName(PrototypeClass.ServiceImpl);
        assertThat(actualPkgName).isNotEmpty();
        assertThat(actualPkgName).isEqualTo(basePackage + ".pet.impl");
    }

    private void initDictionary() {
        // Add a TestFixture for this stuff to make it easier
        Map<PrototypeClass, String> items = new EnumMap<>(PrototypeClass.class);
        items.put(PrototypeClass.AlphabeticField, "infra.validation");
        items.put(PrototypeClass.AlphabeticValidator, "infra.validation");
        items.put(PrototypeClass.ProblemConfiguration, "infra.config");
        items.put(PrototypeClass.ResourcePojo, "domain");
        items.put(PrototypeClass.Routes, "api");
        items.put(PrototypeClass.ServiceApi, "api");
        items.put(PrototypeClass.ServiceImpl, "api.impl");
        items.put(PrototypeClass.Controller, "api");
        classUnderTest.addAll(items);
    }

}
