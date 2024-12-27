package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSetTestFixture;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSetTestFixture;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchetypeDescriptorFactoryTest {

    ArchetypeDescriptorFactory factoryUnderTest;

    @BeforeEach
    public void setUpFactory() throws Exception {
        PackageLayoutRuleSet plrs = PackageLayoutRuleSetTestFixture.packageLayoutRuleSet();
        ClassNameRuleSet cnrs = ClassNameRuleSetTestFixture.classNameRuleSet();
        factoryUnderTest = new ArchetypeDescriptorFactory(plrs, cnrs);
    }

    @Test
    void shouldReturnWellFormedArchetypeDescriptor() {
        JavaArchetypeDescriptor descriptor = factoryUnderTest.createArchetypeDescriptor(Archetype.Application);
        assertThat(descriptor).isNotNull();
        assertThat(descriptor.archetype()).isEqualTo(Archetype.Application);
        assertThat(descriptor.className()).isEqualTo("Application"); // default name of the main class
        assertThat(descriptor.packageName()).isEqualTo("{{basePackage}}");    // as set by the fixture
        assertThat(descriptor.fqcn()).isEqualTo("{{basePackage}}.Application");
    }

    @Test
    void shouldReturnRenamedClass() {
        JavaArchetypeDescriptor descriptor = factoryUnderTest.createArchetypeDescriptor(Archetype.SecureRandomSeries);
        assertThat(descriptor).isNotNull();
        assertThat(descriptor.archetype()).isEqualTo(Archetype.SecureRandomSeries);
        assertThat(descriptor.className()).isEqualTo("ResourceIdGenerator"); // as set in test-classname-rules.properties
        assertThat(descriptor.packageName()).isEqualTo("{{basePackage}}.infra.utils");  // as set in test-package-layout.jon
        assertThat(descriptor.fqcn()).isEqualTo("{{basePackage}}.infra.utils.ResourceIdGenerator");
    }

    @Test
    void shouldResolveEndpointClassNames() {
        JavaArchetypeDescriptor descriptor = factoryUnderTest.createArchetypeDescriptor(Archetype.EntityToPojoConverter, "Book");

        // must return a non-null descriptor
        assertThat(descriptor).isNotNull();

        // must define a fully-qualified classname that contains, somewhere, the restObject's name
        assertThat(descriptor.fqcn()).isNotBlank();
        assertThat(descriptor.fqcn()).contains("Book");
        assertThat(descriptor.fqcn()).startsWith("{{basePackage}}");

        assertThat(descriptor.className()).doesNotEndWith(ClassNameRuleSet.UNDEFINED_SUFFIX);

        assertThat(descriptor.fqcnUnitTest()).isNotBlank();
        assertThat(descriptor.fqcnUnitTest()).contains("Book");

        assertThat(descriptor.fqcnIntegrationTest()).isNotBlank();
        assertThat(descriptor.fqcnIntegrationTest()).contains("Book");


    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionIfPackageLayoutRuleSetIsNull(PackageLayoutRuleSet ruleSet) {
        // Because the fixture's method could throw IOException which gets in the way of this NPE test
        ClassNameRuleSet mockClassRules = Mockito.mock(ClassNameRuleSet.class);
        assertThrows(NullPointerException.class,
                () -> new ArchetypeDescriptorFactory(ruleSet, mockClassRules));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionIfClassNameRuleSetIsEmpty(ClassNameRuleSet ruleSet) {
        // Because the fixture's method could throw IOException which gets in the way of this NPE test
        PackageLayoutRuleSet mockRuleSet = Mockito.mock(PackageLayoutRuleSet.class);
        assertThrows(NullPointerException.class,
                () -> new ArchetypeDescriptorFactory(mockRuleSet, ruleSet));
    }

    @Test
    void verifyToString() {
        assertThat(factoryUnderTest.toString()).isNotBlank();
    }

    @Test
    void verifyToStringOnJavaArchetypeDescriptor() {
        assertThat(factoryUnderTest.createArchetypeDescriptor(Archetype.Application).toString()).isNotBlank();
    }
}
