package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSetTestFixture;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSet;
import mmm.coffee.metacode.common.dictionary.functions.PackageLayoutRuleSetTestFixture;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArchetypeDescriptorFactoryTest {

    ArchetypeDescriptorFactory factoryUnderTest;

    @BeforeEach
    public void setUpFactory() {
        PackageLayoutRuleSet plrs = PackageLayoutRuleSetTestFixture.basicRuleSet();
        ClassNameRuleSet cnrs = ClassNameRuleSetTestFixture.basicRuleSet();
        factoryUnderTest = new ArchetypeDescriptorFactory(plrs, cnrs);
    }

    @Test
    void shouldReturnWellFormedArchetypeDescriptor() {
        JavaArchetypeDescriptor descriptor = factoryUnderTest.createArchetypeDescriptor(Archetype.Application);
        assertThat(descriptor).isNotNull();
        assertThat(descriptor.archetype()).isEqualTo(Archetype.Application);
        assertThat(descriptor.className()).isEqualTo("Application"); // default name of the main class
        assertThat(descriptor.packageName()).isEqualTo("com.acme.petstore");    // as set by the fixture
        assertThat(descriptor.fqcn()).isEqualTo("com.acme.petstore.Application");
    }

    @Test
    void shouldReturnRenamedClass() {
        JavaArchetypeDescriptor descriptor = factoryUnderTest.createArchetypeDescriptor(Archetype.SecureRandomSeries);
        assertThat(descriptor).isNotNull();
        assertThat(descriptor.archetype()).isEqualTo(Archetype.SecureRandomSeries);
        assertThat(descriptor.className()).isEqualTo("ResourceIdGenerator"); // default name of the main class
        assertThat(descriptor.packageName()).isEqualTo("com.acme.petstore.utils");    // as set by the fixture
        assertThat(descriptor.fqcn()).isEqualTo("com.acme.petstore.utils.ResourceIdGenerator");
    }
}
