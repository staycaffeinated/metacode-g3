package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomPropertyAssemblerTest {

    CustomPropertyAssembler assemblerUnderTest = new CustomPropertyAssembler();
    IArchetypeDescriptorFactory factory;

    @BeforeEach
    public void setUp() throws Exception {
        factory = new FakeArchetypeDescriptorFactory();
    }


    @Test
    void shouldAssembleProjectArchetype() {}

    @ParameterizedTest
    @ValueSource(strings = {
            "EntityToPojoConverter",
            "PojoToDocumentConverter",
            "Pojo"
    })
    void shouldAssembleEndpointArchetype(String archetypeName) {

        Map<String,Object> properties = CustomPropertyAssembler.assembleCustomProperties(factory, "com.example.bookstore", "Book");

        assertThat(properties).isNotNull().isNotEmpty();

        JavaArchetypeDescriptor descriptor = (JavaArchetypeDescriptor) properties.get(archetypeName);
        assertThat(descriptor).isNotNull();
        assertThat(descriptor.className()).isNotBlank();
        assertThat(descriptor.className()).doesNotEndWith(ClassNameRuleSet.UNDEFINED_SUFFIX);
        assertThat(descriptor.testClass()).isNotBlank();
        assertThat(descriptor.integrationTestClass()).isNotBlank();

        // an undefined archetype should return null.
        // this kind of error is probably caused by a spelling error or incorrect archetype name
        // (for example, if the package-layout.json uses an archetype name not found in the Archetype class)
        assertThat(properties.get("SoapToDocumentConverter")).isNull();
    }

}
