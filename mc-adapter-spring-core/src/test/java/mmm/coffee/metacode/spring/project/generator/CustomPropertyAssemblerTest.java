package mmm.coffee.metacode.spring.project.generator;

import mmm.coffee.metacode.common.dictionary.ArchetypeDescriptorFactory;
import mmm.coffee.metacode.common.dictionary.IArchetypeDescriptorFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomPropertyAssemblerTest {

    CustomPropertyAssembler assemblerUnderTest = new CustomPropertyAssembler();
    IArchetypeDescriptorFactory factory = new FakeArchetypeDescriptorFactory();


    @Test
    void shouldAssembleProjectArchetype() {}

    @Test
    void shouldAssembleEndpointArchetype() {

        Map<String,Object> properties = CustomPropertyAssembler.assembleCustomProperties(factory, "com.example.bookstore", "Book");

        assertThat(properties).isNotNull().isNotEmpty();
        assertThat(properties.get("EntityToPojoConverter")).isNotNull();

        // an undefined archetype should return null.
        // this kind of error is probably caused by a spelling error or incorrect archetype name
        // (for example, the package-layout.json uses an archetype name not found in the Archetype class
        // todo: let's add something to the ProjectLayoutToMapMapper to report unrecognized archetypes
        assertThat(properties.get("EjbToPojoConverter")).isNull();
    }

}
