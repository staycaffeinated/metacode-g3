package mmm.coffee.metacode.common.dictionary;

import mmm.coffee.metacode.common.model.Archetype;
import mmm.coffee.metacode.common.model.ArchetypeDescriptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndpointArchetypeToMapTest {

    IArchetypeDescriptorFactory descriptorFactory;

    @BeforeEach
    public void setUp() throws Exception {
        descriptorFactory = new FakeArchetypeDescriptorFactory();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "Controller",
            "ConversionService",
            "DatabaseTablePopulator",
            "Document",
            "DocumentTestFixtures",
            "DocumentToPojoConverter",
            "EntityResource",
            "MongoDataStoreApi",
            "ConcreteDataStoreApi",
            "ConcreteDataStoreImpl",
            "PojoToDocumentConverter",
            "Repository",
            "Routes",
            "ServiceApi",
            "ServiceImpl"
    })
    void shouldRecognizeEndpointArchetypes(String archetypeName) {
        Map<String, ArchetypeDescriptor> mapping = EndpointArchetypeToMap.map(descriptorFactory, "Pet");
        assertThat(mapping).isNotEmpty();

        assertThat(mapping.get(archetypeName)).isNotNull();
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenFactoryIsNull(IArchetypeDescriptorFactory factory) {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(factory, "Book"));
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenResourceIsNull(String resource) {
        assertThrows(NullPointerException.class, () -> EndpointArchetypeToMap.map(descriptorFactory, resource));
    }

    @Test
    void shouldContainPojoToDocumentConverter() {
        for (Archetype at : EndpointArchetypeToMap.ENDPOINT_ARCHETYPES) {
            if (at == Archetype.PojoToDocumentConverter) {
                return;
            }
        }
        Assertions.fail("The EndpointArchetypeToMap is missing the PojoToDocumentConverter archetype");
    }
}
