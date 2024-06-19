package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointArchetypeToMapTest {

    IArchetypeDescriptorFactory descriptorFactory = new FakeArchetypeDescriptorFactory();


    @Test
    void shouldWork() {
        Map<String, ArchetypeDescriptor> mapping = EndpointArchetypeToMap.map(descriptorFactory, "Pet");
        assertThat(mapping).isNotEmpty();

        ArchetypeDescriptor descriptor = mapping.get("Controller");

        assertThat(mapping.get("Controller")).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(null, "Book"));
    }

    @Test
    void shouldThrowExceptionWhenResourceIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(descriptorFactory, null));
    }
}
