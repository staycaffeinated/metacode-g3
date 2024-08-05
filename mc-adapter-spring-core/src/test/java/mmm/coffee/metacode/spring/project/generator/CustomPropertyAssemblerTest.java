package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.ProjectArchetypeToMap;
import mmm.coffee.metacode.common.dictionary.functions.ClassNameRuleSet;
import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import mmm.coffee.metacode.common.model.JavaArchetypeDescriptor;
import mmm.coffee.metacode.spring.FakeArchetypeDescriptorFactory;
import org.apache.commons.lang3.arch.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CustomPropertyAssemblerTest {

    IArchetypeDescriptorFactory factory;

    @BeforeEach
    public void setUp() throws Exception {
        factory = new FakeArchetypeDescriptorFactory();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "EntityToPojoConverter",
            "PojoToDocumentConverter",
            "Pojo"
    })
    void shouldAssembleEndpointArchetype(String archetypeName) {

        Map<String, Object> properties = CustomPropertyAssembler.assembleCustomProperties(factory, "com.example.bookstore", "Book");

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

    @Test
    void shouldQuietlyHandleUnknownArchetypeDescriptor() {
        FakeCustomPropertyAssembler assembler = new FakeCustomPropertyAssembler();
        ArchetypeDescriptor mockDescriptor = Mockito.mock(ArchetypeDescriptor.class);

        ArchetypeDescriptor actual = assembler.resolveBasePackage(mockDescriptor, "org.example.petstore");
        assertThat(actual).isNotNull();

        actual = assembler.resolveBasePackage(mockDescriptor, "org.example.hotel", "PetStay");
        assertThat(actual).isNotNull();

    }

    @Test
    void shouldProvideValues() {
        CustomPropertyAssembler.EdgeCaseResolvedArchetypeDescriptor descriptor = CustomPropertyAssembler.EdgeCaseResolvedArchetypeDescriptor.builder()
                .archetype(Archetype.Application)
                .className("Application")
                .fqcn("org.example.petstore.Application")
                .packageName("org.example.petstore")
                .build();

        assertThat(descriptor.fqcnIntegrationTest()).isNotBlank();
        assertThat(descriptor.fqcnIntegrationTest()).doesNotEndWith("Test");
        
        assertThat(descriptor.fqcnUnitTest()).isNotBlank();
        assertThat(descriptor.fqcnUnitTest()).doesNotEndWith("Test");

        assertThat(descriptor.toString()).isNotBlank();

    }


    static class FakeCustomPropertyAssembler extends CustomPropertyAssembler {

        public ArchetypeDescriptor resolveBasePackage(ArchetypeDescriptor descriptor, String basePackage, String restObj) {
            return super.resolveBasePackageOf(descriptor, basePackage, restObj);
        }

        public ArchetypeDescriptor resolveBasePackage(ArchetypeDescriptor descriptor, String basePackage) {
            return super.resolveBasePackageOf(descriptor, basePackage);
        }

    }

}
