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
        classUnderTest.add(ClassKey.ServiceImpl, "pet.impl");
        String actualPkgName = classUnderTest.packageName(ClassKey.ServiceImpl);
        assertThat(actualPkgName).isNotEmpty();
        assertThat(actualPkgName).isEqualTo(basePackage + ".pet.impl");
    }

    private void initDictionary() {
        // Add a TestFixture for this stuff to make it easier
        Map<ClassKey, String> items = new EnumMap<>(ClassKey.class);
        items.put(ClassKey.AlphabeticField, "infra.validation");
        items.put(ClassKey.AlphabeticValidator, "infra.validation");
        items.put(ClassKey.ProblemConfiguration, "infra.config");
        items.put(ClassKey.ResourcePojo, "domain");
        items.put(ClassKey.Routes, "api");
        items.put(ClassKey.ServiceApi, "api");
        items.put(ClassKey.ServiceImpl, "api.impl");
        items.put(ClassKey.Controller, "api");
        classUnderTest.addAll(items);
    }

}
